 
SELECT 
      etablissement, 
      SUM(nombre_premiere_entree) AS nombre_premiere_entree,  -- Nombre de nouveaux arrivants (première entrée)  
      SUM(nombre_mutation_sortant) AS  nombre_mutation_entrant, -- Nombre d'enfants transférés vers cet établissement
      SUM(nombre_mutation_entrant) AS  nombre_mutation_sortant, -- Nombre d'enfants transférés vers un autre établissement
      SUM(nombre_liberation) AS nombre_liberation,   -- Nombre d'enfants libérés
      SUM(nombre_changement_etablissement) AS nombre_changement_etablissement   -- Nombre d'enfants  changé etablissement
      
FROM (
    -- Partie 1 : Nombre d'enfants nouvellement incarcérés (première entrée)
   SELECT 
        etablissement.libelle_etablissement AS etablissement, 
       
       COUNT(*) AS nombre_premiere_entree,  
        0 AS nombre_mutation_entrant, 
        0 AS nombre_mutation_sortant,
        0  AS nombre_liberation,
        0 AS nombre_changement_etablissement
   
    FROM res res
    LEFT JOIN eta etablissement  
        ON res.ETAFK = etablissement.id 
    WHERE num_ord_res = '1' 
    AND TRUNC(res.date_entree) BETWEEN :dateDebut   
                                   AND :dateFin  
    GROUP BY etablissement.libelle_etablissement 
   -- ORDER BY etablissement.libelle_etablissement   

    UNION ALL
--Partie 2 :   Nombre d'enfants transférés (entrants et sortants)
    
    SELECT  
        COALESCE(eta.libelle_etablissement, 'Total Général') AS etablissement, 
       
        0  AS nombre_premiere_entree,  
        COALESCE(depart.nombre_depart, 0) AS nombre_mutation_entrant, 
        COALESCE(arrive.nombre_arrivee, 0) AS nombre_mutation_sortant,
        0  AS nombre_liberation,
        0  AS nombre_changement_etablissement
  
    FROM (
        SELECT DISTINCT libelle_etablissement 
        FROM eta
        WHERE id IN (
            SELECT ETAFK FROM res WHERE TRUNC(date_sortie) BETWEEN :dateDebut 
                                                        AND :dateFin
            UNION
            SELECT ETAFK FROM res WHERE id IN (
                SELECT r2.ETAFK FROM res r1 
                JOIN res r2 ON r1.id_enf = r2.id_enf 
                            AND r1.NUM_ORD_ARR = r2.NUM_ORD_ARR 
                            AND r2.num_ord_res = r1.num_ord_res + 1
                            AND TRUNC(r1.date_sortie) = TRUNC(r2.date_entree)
                WHERE TRUNC(r1.date_sortie) BETWEEN :dateDebut 
                                               AND :dateFin
            )
        )
    ) eta
    LEFT JOIN (
        --2.1: Nombre de mutations entrantes par établissement
        SELECT eta_depart.libelle_etablissement, COUNT(*) AS nombre_depart
        FROM res r1
        JOIN res r2 
          ON r1.id_enf = r2.id_enf 
          AND r1.NUM_ORD_ARR = r2.NUM_ORD_ARR 
          AND r2.num_ord_res = r1.num_ord_res + 1
          AND TRUNC(r1.date_sortie) = TRUNC(r2.date_entree)
        LEFT JOIN eta eta_depart ON eta_depart.id = r1.ETAFK
        WHERE TRUNC(r1.date_sortie) BETWEEN :dateDebut 
                                       AND :dateFin
        GROUP BY eta_depart.libelle_etablissement
    ) depart ON eta.libelle_etablissement = depart.libelle_etablissement
    LEFT JOIN (
        -- Nombre de mutations sortantes par établissement
        SELECT eta_arrive.libelle_etablissement, COUNT(*) AS nombre_arrivee
        FROM res r1
        JOIN res r2 
          ON r1.id_enf = r2.id_enf 
          AND r1.NUM_ORD_ARR = r2.NUM_ORD_ARR 
          AND r2.num_ord_res = r1.num_ord_res + 1
          AND TRUNC(r1.date_sortie) = TRUNC(r2.date_entree)
        LEFT JOIN eta eta_arrive ON eta_arrive.id = r2.ETAFK
        WHERE TRUNC(r1.date_sortie) BETWEEN :dateDebut 
                                       AND :dateFin
        GROUP BY eta_arrive.libelle_etablissement
    ) arrive ON eta.libelle_etablissement = arrive.libelle_etablissement
  --ORDER BY eta.libelle_etablissement
    UNION ALL

    
    -- Partie 3 : Nombre d'enfants libérés
   SELECT 
        etablissement.libelle_etablissement AS etablissement, 
       
        0 AS nombre_premiere_entree, 
        0 AS nombre_mutation_entrant, 
        0 AS nombre_mutation_sortant,
        COUNT(arr.id_enf) AS nombre_liberation,
        0 AS nombre_changement_etablissement
    FROM arr
    LEFT JOIN lib 
        ON arr.id_enf = lib.id_enf 
        AND arr.num_ord = lib.num_ord 
    INNER JOIN (
        SELECT 
            res.id_enf, 
            res.NUM_ORD_ARR, 
            MAX(res.num_ord_res) AS num_ord_res
        FROM res
        WHERE TRUNC(res.date_sortie) BETWEEN :dateDebut 
                                         AND :dateFin
        GROUP BY res.id_enf, res.NUM_ORD_ARR
    ) res_id 
    ON arr.id_enf = res_id.id_enf  
    AND arr.num_ord = res_id.NUM_ORD_ARR
    INNER JOIN res 
        ON res.id_enf = res_id.id_enf 
        AND res.NUM_ORD_ARR = res_id.NUM_ORD_ARR 
        AND res.num_ord_res = res_id.num_ord_res
        AND res.etab_change IS  NULL
    LEFT JOIN eta etablissement  
        ON res.ETAFK = etablissement.id
    WHERE TRUNC(lib.date_liberation) BETWEEN :dateDebut 
                                         AND :dateFin
    AND :dateFin <= SYSDATE
    GROUP BY etablissement.libelle_etablissement 
    
      UNION ALL
      -- Partie 4 : Nombre d'enfants changé etablissement
   SELECT  
etablissement.libelle_etablissement AS etablissement, 
        0 AS nombre_premiere_entree, 
        0 AS nombre_mutation_entrant, 
        0 AS nombre_mutation_sortant,
        0 AS nombre_liberation,
       COUNT(*) AS nombre_changement_etablissement
        
FROM res
 LEFT JOIN eta etablissement  
        ON res.ETAFK = etablissement.id
WHERE 
   TRUNC(res.date_sortie) BETWEEN :dateDebut 
                                     AND :dateFin
AND res.etab_change IS NOT NULL
GROUP BY etablissement.libelle_etablissement
) grouped
GROUP BY etablissement
ORDER BY etablissement