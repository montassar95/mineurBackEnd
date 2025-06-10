
--DEFINE tnumide = '0100147670'
---DEFINE tcoddet =  '002'


WITH affaire_data AS (
    SELECT 
    
        TAFF.tnumseqaff,
       TR.nature_tribunal,
        TR.libelle_tribunal,
        SUBSTR(TAFF.tnumjaf, 4, 6) || ' - ' || SUBSTR(TAFF.tnumjaf, 1, 3) AS tnumjaf_formatte,
        TAFF.typema,
        CASE 
            WHEN TAFF.TCODSIT = 'F' THEN 'مغـلـقـة'
            WHEN TAFF.TCODSIT = 'O' THEN 'جـاريـة'
            WHEN TAFF.TCODSIT = 'T' THEN 'محـالــــة'
            WHEN TAFF.TCODSIT = 'ط' THEN 'طعــــــن'
            WHEN TAFF.TCODSIT = 'ن' THEN 'إستئناف النيابة'
            WHEN TAFF.TCODSIT = 'ت' THEN 'تعقيـــب'
            WHEN TAFF.TCODSIT = 'L' THEN 'ســـراح'
            ELSE TAFF.TCODSIT 
        END AS etat_affaire,
        NA.libelle_nature,
         NA.type_affaire ,
        TAFF.TTYPRES || ' ' || TAFF.TCODRES || ' ' || TAFF.TANNRES AS numero_ecrou,
        CASE 
            WHEN TAFF.typema = '1' THEN 'بطاقة إيداع'
            WHEN TAFF.typema = '2' THEN 'بطاقة جلب'
            WHEN TAFF.typema = '3' THEN 'مضمون حكم'
            WHEN TAFF.typema = '4' THEN 'جبــر بالسّجــن'
            WHEN TAFF.typema = 'T' THEN 'إحـــالة '
            ELSE TAFF.typema 
        END AS type_mandat,

        -- Accusations concaténées
        CASE 
        WHEN TAFF.typema = '3' 
             THEN (
                SELECT LISTAGG(f.libelle_famille_acc , ' و ')
                WITHIN GROUP (ORDER BY t.tcodacc)
                FROM taccusation@DBLINKMINEURPROD t
                JOIN famille_accusation@DBLINKMINEURPROD f ON t.tcodfac = f.code_famille_acc
                WHERE t.tnumide = TAFF.tnumide 
                  AND t.tcoddet = TAFF.tcoddet 
                  AND t.tcodextj = TAFF.tntypema
             )  
             
            WHEN TAFF.typema = '1' 
             THEN (
                SELECT t.ttextma 
                FROM TMANDATDEPOT@DBLINKMINEURPROD t
                
                WHERE t.tnumide = TAFF.tnumide 
                  AND t.tcoddet = TAFF.tcoddet 
                  AND t.tcodma = TAFF.tntypema
             ) 
             
             WHEN TAFF.typema = 'T' 
             THEN (
                SELECT t.ttextma 
                FROM TMANDATDEPOT@DBLINKMINEURPROD t
                
                WHERE t.tnumide = TAFF.tnumide 
                  AND t.tcoddet = TAFF.tcoddet 
                  AND t.tcodma = TAFF.tntypema
             ) 
             
             WHEN TAFF.typema = '4' 
             THEN (
                SELECT f.libelle_famille_acc 
                FROM tcontrainte@DBLINKMINEURPROD  t
                JOIN famille_accusation@DBLINKMINEURPROD  f ON t.tcodfac = f.code_famille_acc
                WHERE t.tnumide = TAFF.tnumide 
                  AND t.tcoddet = TAFF.tcoddet 
                  AND t.tcodcon = TAFF.tntypema
             ) 
             ELSE NULL 
        END AS accusations_concatenees,

        -- Dates min / max
        CASE WHEN TAFF.typema = '3' 
             THEN (
                SELECT MIN(t.tdatdacc)
                FROM taccusation@DBLINKMINEURPROD t
                WHERE t.tnumide = TAFF.tnumide 
                  AND t.tcoddet = TAFF.tcoddet 
                  AND t.tcodextj = TAFF.tntypema
             ) 
             ELSE NULL 
        END AS date_debut_min,

        CASE WHEN TAFF.typema = '3' 
             THEN (
                SELECT MAX(t.tdatfacc)
                FROM taccusation@DBLINKMINEURPROD t
                WHERE t.tnumide = TAFF.tnumide 
                  AND t.tcoddet = TAFF.tcoddet 
                  AND t.tcodextj = TAFF.tntypema
             ) 
             ELSE NULL 
        END AS date_fin_max,

        -- Durées cumulées
        CASE WHEN TAFF.typema = '3' 
             THEN (
                SELECT SUM(t.tduracca)
                FROM taccusation@DBLINKMINEURPROD t
                WHERE t.tnumide = TAFF.tnumide 
                  AND t.tcoddet = TAFF.tcoddet 
                  AND t.tcodextj = TAFF.tntypema
             )
             ELSE 0 
        END AS total_annees_raw,

        CASE WHEN TAFF.typema = '3' 
             THEN (
                SELECT SUM(t.tduraccm)
                FROM taccusation@DBLINKMINEURPROD t
                WHERE t.tnumide = TAFF.tnumide 
                  AND t.tcoddet = TAFF.tcoddet 
                  AND t.tcodextj = TAFF.tntypema
             )
             ELSE 0 
        END AS total_mois_raw,

        CASE WHEN TAFF.typema = '3' 
             THEN (
                SELECT SUM(t.tduraccj)
                FROM taccusation@DBLINKMINEURPROD t
                WHERE t.tnumide = TAFF.tnumide 
                  AND t.tcoddet = TAFF.tcoddet 
                  AND t.tcodextj = TAFF.tntypema
             )
             
               WHEN TAFF.typema = '4' 
             THEN (
                SELECT SUM(t.TDURJCAL)
                FROM tcontrainte@DBLINKMINEURPROD  t
                WHERE t.tnumide = TAFF.tnumide 
                  AND t.tcoddet = TAFF.tcoddet 
                     AND t.tcodcon = TAFF.tntypema
             )
             
             ELSE 0 
        END AS total_jours_raw,
        
CASE

   WHEN TAFF.typema = '3' 
             THEN (
            select LIBELLE_TJUGEMENT FROM typejugement@DBLINKMINEURPROD typejugement where typejugement.CODE_TJUGEMENT =
 
              (select t.tcodtju
                FROM tjugement@DBLINKMINEURPROD t
                WHERE t.tnumide = TAFF.tnumide 
                  AND t.tcoddet = TAFF.tcoddet 
                  AND t.tcodextj = TAFF.tntypema)
             )
   WHEN TAFF.typema = '4' THEN (
        SELECT 
            CASE 
                WHEN motif.tlibmot IS NOT NULL THEN
                    motif.tlibmot || ' ' || TO_CHAR(t.tdatfpe, 'DD-MM-YYYY')
                ELSE NULL
            END
        FROM TCONTRAINTE@DBLINKMINEURPROD t
        LEFT JOIN tmotif@DBLINKMINEURPROD motif ON motif.tcodmot = t.tcodmot
        WHERE t.tnumide = TAFF.tnumide 
          AND t.tcoddet = TAFF.tcoddet 
          AND t.tcodcon = TAFF.tntypema
    )
             ELSE null 
        END AS libelle_jugement ,
        
        CASE WHEN TAFF.typema = '3' 
             THEN (
            select TYPE_TJUGEMENT FROM typejugement@DBLINKMINEURPROD typejugement where typejugement.CODE_TJUGEMENT =
 
              (select t.tcodtju
                FROM tjugement@DBLINKMINEURPROD t
                WHERE t.tnumide = TAFF.tnumide 
                  AND t.tcoddet = TAFF.tcoddet 
                  AND t.tcodextj = TAFF.tntypema)
             )
             ELSE null 
        END AS type_jugement ,
        CASE WHEN TAFF.typema = '3' 
             THEN (
            select nature_jugement FROM typejugement@DBLINKMINEURPROD typejugement where typejugement.CODE_TJUGEMENT =

              (select t.tcodtju
                FROM tjugement@DBLINKMINEURPROD t
                WHERE t.tnumide = TAFF.tnumide 
                  AND t.tcoddet = TAFF.tcoddet 
                  AND t.tcodextj = TAFF.tntypema)
             )
             ELSE null 
        END AS nature_jugement ,
        
        
        TAFF.tntypema,
        TAFF.tdatdep,
        TAFF.TCODSIT,
         
        ROW_NUMBER() OVER (
            PARTITION BY TAFF.tnumseqaff  
            ORDER BY  
                TAFF.tdatdep DESC,
                CASE TAFF.TCODSIT  
                    WHEN 'O' THEN 1 
                    WHEN 'L' THEN 2 
                    WHEN 'ت' THEN 3 
                    WHEN 'ن' THEN 4 
                    WHEN 'ط' THEN 5 
                    WHEN 'F' THEN 6 
                    ELSE 7 
                END
        ) AS rn
    FROM TIDEAFF@DBLINKMINEURPROD TAFF
    JOIN tribunal@DBLINKMINEURPROD TR ON TAFF.tcodtri = TR.code_tribunal
    JOIN natureAffaire@DBLINKMINEURPROD NA ON TAFF.tcodtaf = NA.code_nature
   -- WHERE TAFF.tnumide = &tnumide AND TAFF.tcoddet = &tcoddet
      WHERE TAFF.tnumide = :tnumide AND TAFF.tcoddet = :tcoddet
),


affaire_filtree AS (
    SELECT *
    FROM affaire_data
    WHERE rn = 1
)

-- ✅ Sélection : affaires normales + ligne de total
,
 affaire_fin as (
-- ✅ Sélection : affaires normales + ligne de total
SELECT 
    tnumseqaff,
    libelle_nature,                            -- Libellé de la nature de l’affaire, type: chaîne
     libelle_tribunal,                          -- Nom du tribunal, type: chaîne
   
    tnumjaf_formatte,                          -- Numéro JAF formaté, type: chaîne
    accusations_concatenees,                   -- Liste des accusations, type: chaîne
    etat_affaire,                              -- État de l’affaire, type: chaîne
    type_mandat,                               -- Type de mandat (ex. DP, HB...), type: chaîne
  TO_CHAR(tdatdep, 'YYYY-MM-DD'),
    -- Durée totale des peines (transformée en années, mois, jours)
    TRUNC(SUM(total_annees_raw) + FLOOR((SUM(total_mois_raw) + FLOOR(SUM(total_jours_raw)/30)) / 12)) AS total_annees,  -- type: numérique
    MOD(FLOOR(SUM(total_mois_raw) + FLOOR(SUM(total_jours_raw)/30)), 12) AS total_mois,                                  -- type: numérique
    MOD(SUM(total_jours_raw), 30) AS total_jours,                                                                         -- type: numérique

    libelle_jugement,                          -- Libellé du jugement (condamnation, relaxe, etc.), type: chaîne
    numero_ecrou,                              -- Numéro d’écrou du détenu, type: chaîne
    TO_CHAR(date_debut_min, 'YYYY-MM-DD') AS date_debut_min,  -- Date de début de peine la plus ancienne, type: date
    TO_CHAR(date_fin_max, 'YYYY-MM-DD') AS date_fin_max,      -- Date de fin de peine la plus récente, type: date

    typema AS type_document,                   -- Type de document juridique (1, T, 3...), type: chaîne
    TCODSIT,                                   -- Situation de jugement (L, F...), type: chaîne
    type_jugement,                             -- Type du jugement, type: chaîne
    nature_jugement,                           -- Nature du jugement, type: chaîne
    nature_tribunal,                           -- Nature du tribunal, type: chaîne ou numérique déguisé
    type_affaire ,                              -- Type d'affaire, type: chaîne ou numérique,
  --   tntypema,-- Identifiant séquentiel de l’affaire, type: chaîne
   
    ROW_NUMBER() OVER (
            ORDER BY 
    -- 1. Situation du jugement (TCODSIT), type: texte ('L' = liberté, 'F' = fermé, autre)
    CASE 
        WHEN TCODSIT = 'L' THEN 0
        WHEN TCODSIT = 'F' THEN 1
        ELSE 2
    END DESC,  -- alias: ordre_tcodsit
 -- 5. Type de document (typema), ordre spécifique, type: texte
    CASE 
        WHEN typema = '1' THEN 1
        WHEN typema = 'T' THEN 2
        WHEN typema = '3' AND type_jugement = '0' THEN 3
        WHEN typema = '3' AND type_jugement != '0' THEN 4
        WHEN typema = '2' THEN 5
        WHEN typema = '4' THEN 6
        ELSE 6
    END ASC,  -- alias: ordre_typema
    -- 2. Type de jugement numérique, avec fallback sur 999999 si égal à '0', type: numérique
    CASE 
        WHEN type_jugement = '0'  THEN 999999
        ELSE TO_NUMBER(type_jugement)
    END,  -- alias: ordre_type_jugement

    -- 3. Nature du jugement (1 à 4), type: numérique
    CASE 
        WHEN nature_jugement = '1' THEN 1
        WHEN nature_jugement = '2' THEN 2
        WHEN nature_jugement = '3' THEN 3
        WHEN nature_jugement = '4' THEN 4
        ELSE -1
    END DESC,  -- alias: ordre_nature_jugement

    -- 4. Type d’affaire, avec 0 remplacé par 99999, type: numérique
    CASE 
        WHEN TO_NUMBER(type_affaire) = 0 THEN 99999
        ELSE TO_NUMBER(type_affaire)
    END ASC,  -- alias: ordre_type_affaire





    -- 7. Nature du tribunal, type: numérique avec cas spéciaux
    CASE 
        WHEN nature_tribunal IN ('00', '0') THEN 999999
        ELSE TO_NUMBER(nature_tribunal)
    END,  -- alias: ordre_nature_tribunal

    -- 8. Durée totale : années, mois, jours, type: numérique
    TRUNC(SUM(total_annees_raw) + FLOOR((SUM(total_mois_raw) + FLOOR(SUM(total_jours_raw)/30)) / 12)) DESC,  -- alias: duree_annees
    MOD(FLOOR(SUM(total_mois_raw) + FLOOR(SUM(total_jours_raw)/30)), 12) DESC,                                -- alias: duree_mois
    MOD(SUM(total_jours_raw), 30) DESC   ,                                                                    -- alias: duree_jours
    tdatdep
        ) AS row_affaire_principale,
  
   ROW_NUMBER() OVER (
                 ORDER BY tnumseqaff   
              ) AS row_order
FROM affaire_filtree

GROUP BY
    tnumseqaff,
    tntypema,
    libelle_nature,
    libelle_tribunal,
    tnumjaf_formatte,
    accusations_concatenees,
    etat_affaire,
    type_mandat,
    tdatdep,
    libelle_jugement,
    numero_ecrou,
    date_debut_min,
    date_fin_max,
    typema,
    TCODSIT,
    type_jugement,
    nature_jugement,
    nature_tribunal,
    type_affaire

----------------------------------------------------------------------------------------

--- ajouter ici union all qui affiche totlae jour mois et anne 

    -----------------------------------------------------------------------

    )
     
    


select *
from affaire_fin   WHERE row_order BETWEEN :min_page AND :max_page

 ORDER BY row_order

 
 