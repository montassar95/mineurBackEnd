
 --DEFINE tnumide = '0400049223'
-- DEFINE tcoddet =  '001'


WITH affaire_data AS (
    SELECT 
    
        TAFF.tnumseqaff,
        TR.nature_tribunal,
        TR.libelle_tribunal,
        TAFF.tnumjaf ,
        TAFF.typema,
        
        NA.libelle_nature,
         NA.type_affaire ,
        
       

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
             ELSE 0 
        END AS total_jours_raw,
        
 
        
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
   
    tnumjaf,                          -- Numéro JAF formaté, type: chaîne
    accusations_concatenees,                   -- Liste des accusations, type: chaîne
                             -- État de l’affaire, type: chaîne
                              -- Type de mandat (ex. DP, HB...), type: chaîne
  TO_CHAR(tdatdep, 'YYYY-MM-DD') as tdatdep,
    -- Durée totale des peines (transformée en années, mois, jours)
    TRUNC(SUM(total_annees_raw) + FLOOR((SUM(total_mois_raw) + FLOOR(SUM(total_jours_raw)/30)) / 12)) AS total_annees,  -- type: numérique
    MOD(FLOOR(SUM(total_mois_raw) + FLOOR(SUM(total_jours_raw)/30)), 12) AS total_mois,                                  -- type: numérique
    MOD(SUM(total_jours_raw), 30) AS total_jours,                                                                         -- type: numérique

                            -- Libellé du jugement (condamnation, relaxe, etc.), type: chaîne
                             -- Numéro d’écrou du détenu, type: chaîne
    

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
        ) AS row_affaire_principale 
FROM affaire_filtree

GROUP BY
    tnumseqaff,
    tntypema,
    libelle_nature,
    libelle_tribunal,
    tnumjaf,
    accusations_concatenees,
  
 
    tdatdep,
     
 
    
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
     
    


SELECT       
    af.tnumseqaff,  
    af.libelle_tribunal,                          
    SUBSTR(af.tnumjaf, 4, 6) || ' - ' || SUBSTR(af.tnumjaf, 1, 3) AS tnumjaf_formatte,                        
    af.accusations_concatenees,                  
    af.tdatdep,     
    af.libelle_nature,
    NULL AS totale
FROM affaire_fin af     
WHERE row_affaire_principale = 1
 
 FETCH FIRST 1 ROWS ONLY
 
 