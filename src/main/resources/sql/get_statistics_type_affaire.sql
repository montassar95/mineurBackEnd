 

WITH cte_statut AS (
   SELECT 
        arr.id_enf, 
        arr.num_ord, 
        res.num_ord_res, 
        res.num_arrestation, 
        res.ETAFK, 
        arr.date_arrestation, 
          'Residant'  AS statut
    
    
    FROM arr arr
    LEFT JOIN lib lib 
        ON arr.id_enf = lib.id_enf 
        AND arr.num_ord = lib.num_ord 
    LEFT JOIN (
        SELECT 
            res.id_enf, 
            res.NUM_ORD_ARR, 
            MAX(res.num_ord_res) AS num_ord_res
        FROM res
        WHERE TRUNC(res.date_entree) <= :dateFin  
        AND ( TRUNC(res.date_sortie) >  :dateFin  OR res.date_sortie IS NULL)
        GROUP BY res.id_enf, res.NUM_ORD_ARR
    ) res_id 
    INNER JOIN res res 
        ON res.id_enf = res_id.id_enf 
        AND res.NUM_ORD_ARR = res_id.NUM_ORD_ARR 
        AND res.num_ord_res = res_id.num_ord_res
        
    ON arr.id_enf = res.id_enf  
    AND arr.num_ord = res.NUM_ORD_ARR
    LEFT JOIN CAU_LIB cau_lib 
        ON lib.CAU_LIBFK = cau_lib.id  -- Jointure avec CAU_LIB
      WHERE 
      TRUNC(res.date_entree) <= :dateFin AND 
       ( TRUNC(lib.date_liberation) > :dateFin  OR lib.date_liberation IS NULL)

),

-- CTE pour obtenir le dernier document par arrestation pour une période donnée
last_document AS (
    SELECT 
        doc.id_enf, 
        doc.num_ord_arr, 
        doc.num_ord_aff, 
        MAX(doc.num_ord_doc) AS max_num_ord_doc 
    FROM cte_statut cte
    LEFT JOIN doc doc 
        ON cte.id_enf = doc.id_enf
        AND cte.num_ord = doc.num_ord_arr
        AND TRUNC(doc.date_emission) <= :dateFin
    GROUP BY doc.id_enf, doc.num_ord_arr, doc.num_ord_aff    
),

-- CTE pour obtenir le dernier document par arrestation et affaire pour une période donnée
last_document_by_aff AS (
    SELECT 
        ld.id_enf, 
        ld.num_ord_arr, 
        ld.num_ord_aff,
        ld.max_num_ord_doc,
        MAX(doc.num_ord_doc_by_aff) AS max_num_ord_doc_by_aff  
    FROM last_document ld
    LEFT JOIN doc doc 
        ON ld.id_enf = doc.id_enf 
        AND ld.num_ord_arr = doc.num_ord_arr 
        AND ld.num_ord_aff = doc.num_ord_aff 
        AND ld.max_num_ord_doc = doc.num_ord_doc
        AND TRUNC(doc.date_emission) <= :dateFin
    GROUP BY ld.id_enf, ld.num_ord_arr, ld.num_ord_aff ,ld.max_num_ord_doc
) , 
etablissement_situation_judiciaire AS (
SELECT 
        cte.id_enf, 
       
        CASE
            WHEN cte.statut = 'Libre' THEN 'Libre'
            WHEN LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CD%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CH%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%AE%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%AP%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%T%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CJA%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CP%'
            THEN 'arrete'
            WHEN LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CJ%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CR%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CRR%'
            THEN 'juge'
            WHEN LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%ArretEx%'
            THEN 'seraLibre'
            ELSE NULL
        END AS situation_judiciaire_id,
        
        
        
        
        
       
    
  max(typ_aff.STATUT_EXCEPTION  )   as MAX_STATUT_EXCEPTION
    
    FROM cte_statut cte
     LEFT JOIN eta etablissement  
        ON cte.ETAFK = etablissement.id
    LEFT JOIN last_document_by_aff ldba
        ON cte.id_enf = ldba.id_enf 
        AND cte.num_ord = ldba.num_ord_arr
    LEFT JOIN doc doc 
        ON ldba.id_enf = doc.id_enf 
        AND ldba.num_ord_arr = doc.num_ord_arr 
        AND ldba.num_ord_aff = doc.num_ord_aff 
        AND ldba.max_num_ord_doc_by_aff = doc.num_ord_doc_by_aff
        
        LEFT JOIN typ_aff typ_aff 
    ON typ_aff.id = doc.TYPFK
    GROUP BY cte.id_enf , cte.ETAFK, cte.statut   ),
    
    
    
    
    affaire_principale AS (
    SELECT 
    
    COALESCE(situation_judiciaire_id, 'NULL') AS situation_judiciaire_id,
    MAX_STATUT_EXCEPTION,
    COUNT(*) AS nombre_enfants,
    CASE 
        WHEN situation_judiciaire_id IS NULL 
        THEN LISTAGG(id_enf, ', ') WITHIN GROUP (ORDER BY id_enf) 
        ELSE NULL 
    END AS ids_enfants
FROM etablissement_situation_judiciaire
GROUP BY  situation_judiciaire_id, MAX_STATUT_EXCEPTION
 )


SELECT 
    typ_aff.LIBELLE_TYPE_AFFAIRE AS libelle,
    SUM(CASE WHEN situation_judiciaire_id = 'arrete' THEN nombre_enfants ELSE 0 END) AS arrete,
    SUM(CASE WHEN situation_judiciaire_id = 'juge' THEN nombre_enfants ELSE 0 END) AS juge,
    MAX(MAX_STATUT_EXCEPTION) AS max_statut
FROM affaire_principale 
LEFT JOIN typ_aff typ_aff 
    ON typ_aff.STATUT_EXCEPTION = affaire_principale.MAX_STATUT_EXCEPTION
GROUP BY typ_aff.LIBELLE_TYPE_AFFAIRE

UNION ALL

SELECT 
    'المجموع' AS libelle,
    SUM(CASE WHEN situation_judiciaire_id = 'arrete' THEN nombre_enfants ELSE 0 END),
    SUM(CASE WHEN situation_judiciaire_id = 'juge' THEN nombre_enfants ELSE 0 END),
    0 -- Pas de max_statut pour la ligne totale
FROM affaire_principale 

ORDER BY max_statut DESC NULLS LAST, libelle



     