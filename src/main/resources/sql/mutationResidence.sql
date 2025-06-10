 WITH mutation_data AS (
    SELECT 
        mut.TNUMIDE AS prisoner_id,
        mut.TCODDET AS numro_detention,
        mut.TCODMUT AS numero_sequentielle,
        mut.TTYPRES || '  ' || mut.TCODRES || '  ' || mut.TANNRES AS numero_ecrou,
        GETLIBELLEPRISON@DBLINKMINEURPROD(mut.TCODGOU, mut.TCODPR) AS prision,
        LAG(mut.TDATFMU) OVER (ORDER BY mut.TCODMUT) AS raw_date_debut,
        mut.TDATMUT AS raw_date_fin,
        mut.TDATMUT AS raw_date_mutation,
         motif.tlibmot as motif_mutation,
        ROW_NUMBER() OVER (ORDER BY mut.TCODMUT) AS rn
    FROM TMUTATION@DBLINKMINEURPROD mut
    LEFT JOIN tmotif@DBLINKMINEURPROD  motif ON motif.tcodmot = mut.tcodmot 
    WHERE mut.TNUMIDE = :prisonerId AND mut.TCODDET = :detentionCode
),
debut_detention AS (
    SELECT tdatdet 
    FROM tdetention@DBLINKMINEURPROD 
    WHERE TNUMIDE = :prisonerId AND TCODDET = :detentionCode
),
final_mutation AS (
    SELECT 
        CAST(prisoner_id AS VARCHAR2(20)) AS prisoner_id,
        CAST(numro_detention AS VARCHAR2(10)) AS numro_detention,
        CAST(numero_sequentielle AS VARCHAR2(10)) AS numero_sequentielle,
        CAST(numero_ecrou AS VARCHAR2(100)) AS numero_ecrou,
        CAST(prision AS VARCHAR2(100)) AS prision,
        CAST(TO_CHAR(
            CASE 
                WHEN rn = 1 THEN (SELECT tdatdet FROM debut_detention)
                ELSE raw_date_debut
            END, 'YYYY-MM-DD') AS VARCHAR2(20)) AS date_debut,
        CAST(TO_CHAR(raw_date_fin, 'YYYY-MM-DD') AS VARCHAR2(20)) AS date_fin,
        CAST(TO_CHAR(raw_date_mutation, 'YYYY-MM-DD') AS VARCHAR2(20)) AS date_mutation,
        
         CAST(motif_mutation AS VARCHAR2(100)) AS motif_mutation 
    FROM mutation_data
),
final_union AS (
    SELECT * FROM final_mutation
    UNION ALL
   SELECT 
    CAST(res.TNUMIDE AS VARCHAR2(20)) AS prisoner_id,
    CAST(res.TCODDET AS VARCHAR2(10)) AS numro_detention,
    CAST(' ' AS VARCHAR2(10)) AS numero_sequentielle,
    CAST(res.TTYPRES || '  ' || res.TCODRES || '  ' || res.TANNRES AS VARCHAR2(100)) AS numero_ecrou,
    CAST(GETLIBELLEPRISON@DBLINKMINEURPROD(res.TCODGOU, res.TCODPR) AS VARCHAR2(100)) AS prision,
    CAST(TO_CHAR(res.TDATDR, 'YYYY-MM-DD') AS VARCHAR2(20)) AS date_debut,
    CAST(TO_CHAR(res.TDATFR, 'YYYY-MM-DD') AS VARCHAR2(20)) AS date_fin,
    CAST(' ' AS VARCHAR2(20)) AS date_mutation,
    CAST(' ' AS VARCHAR2(10)) AS motif_mutation 
FROM TRESIDENCE@DBLINKMINEURPROD res
WHERE res.ROWID IN (
    SELECT ROWID
    FROM (
        SELECT r.ROWID
        FROM TRESIDENCE@DBLINKMINEURPROD r
        WHERE r.TNUMIDE = :prisonerId 
          AND r.TCODDET = :detentionCode
        ORDER BY 
            CASE WHEN r.TDATFR IS NULL THEN 0 ELSE 1 END,  -- priorité à NULL
            r.TDATFR DESC  -- sinon max date fin
    )
    WHERE ROWNUM = 1
)

)

-- Numérotation finale
SELECT 
    ROW_NUMBER() OVER (ORDER BY date_debut, date_fin  ) AS numero_ligne,
    final_union.*
FROM final_union