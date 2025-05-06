WITH cte_statut AS (
   SELECT 
        arr.id_enf, 
        arr.num_ord, 
        res.num_ord_res, 
        res.num_arrestation, 
        res.ETAFK, 
        arr.date_arrestation, 
        enf.sexe,
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
        WHERE TRUNC(res.date_entree) <=  :dateFin  
        AND ( TRUNC(res.date_sortie) >   :dateFin   OR res.date_sortie IS NULL)
        GROUP BY res.id_enf, res.NUM_ORD_ARR
    ) res_id 
    INNER JOIN res res 
        ON res.id_enf = res_id.id_enf 
        AND res.NUM_ORD_ARR = res_id.NUM_ORD_ARR 
        AND res.num_ord_res = res_id.num_ord_res
        
    ON arr.id_enf = res.id_enf  
    AND arr.num_ord = res.NUM_ORD_ARR
    LEFT JOIN enf enf 
        ON enf.id = arr.id_enf  -- Jointure avec CAU_LIB
      WHERE 
      TRUNC(res.date_entree) <=  :dateFin  AND 
       ( TRUNC(lib.date_liberation) >  :dateFin   OR lib.date_liberation IS NULL)

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
        AND TRUNC(doc.date_emission) <=  :dateFin 
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
        AND TRUNC(doc.date_emission) <=  :dateFin 
    GROUP BY ld.id_enf, ld.num_ord_arr, ld.num_ord_aff ,ld.max_num_ord_doc
)  , alll as(  SELECT 
        cte.id_enf,  
    tribunal.GOUFK,
    gouvernement.LIBELLE_GOUVERNORAT,
     cte.sexe,
        CASE
            WHEN cte.statut = 'Libre' THEN 'Libre'
            WHEN LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CD%' 
              OR LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CH%' 
              OR LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%AE%' 
              OR LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%AP%' 
              OR LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%T%' 
              OR LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CJA%' 
              OR LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CP%' 
            THEN 'arrete'
            WHEN LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CJ%' 
              OR LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CR%' 
              OR LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CRR%' 
            THEN 'juge'
            WHEN LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%ArretEx%' 
            THEN 'seraLibre'
            ELSE NULL
        END AS situation_judiciaire_id 
    FROM cte_statut cte
    
    LEFT JOIN last_document_by_aff ldba
        ON cte.id_enf = ldba.id_enf 
        AND cte.num_ord = ldba.num_ord_arr
    LEFT JOIN doc doc 
        ON ldba.id_enf = doc.id_enf 
        AND ldba.num_ord_arr = doc.num_ord_arr 
        AND ldba.num_ord_aff = doc.num_ord_aff 
        AND ldba.max_num_ord_doc_by_aff = doc.num_ord_doc_by_aff 
    LEFT JOIN tri tribunal
      ON tribunal.id = doc.A_ID_TRI 
    LEFT JOIN gou gouvernement 
      ON gouvernement.id = tribunal.GOUFK
    GROUP BY cte.id_enf,     tribunal.GOUFK, gouvernement.LIBELLE_GOUVERNORAT ,  cte.sexe, cte.statut ) 
 
, ranked AS (
    SELECT 
        alll.*, 
        ROW_NUMBER() OVER (
            PARTITION BY id_enf 
            ORDER BY 
                CASE situation_judiciaire_id 
                    WHEN 'arrete' THEN 1
                    WHEN 'juge' THEN 2
                    WHEN 'seraLibre' THEN 3
                    ELSE 4 -- au cas où il y aurait d'autres valeurs
                END
        ) AS rn
    FROM alll
)


SELECT  
    GOUFK,
    LIBELLE_GOUVERNORAT,
    SUM(CASE WHEN situation_judiciaire_id = 'arrete' AND sexe = 'ذكر' THEN 1 ELSE 0 END) AS arretemasculin,
    SUM(CASE WHEN situation_judiciaire_id = 'arrete' AND sexe = 'أنثى' THEN 1 ELSE 0 END) AS arretefeminin,
    SUM(CASE WHEN situation_judiciaire_id = 'juge' AND sexe = 'ذكر' THEN 1 ELSE 0 END) AS jugemasculin,
    SUM(CASE WHEN situation_judiciaire_id = 'juge' AND sexe = 'أنثى' THEN 1 ELSE 0 END) AS jugefeminin,
    SUM(CASE 
            WHEN situation_judiciaire_id NOT IN ('arrete', 'juge') OR situation_judiciaire_id IS NULL 
            THEN 1 
            ELSE 0 
        END) AS autre
FROM ranked
WHERE rn = 1 
GROUP BY GOUFK, LIBELLE_GOUVERNORAT

UNION ALL

SELECT  
    NULL AS GOUFK,
    'المجموع' AS LIBELLE_GOUVERNORAT,
    SUM(CASE WHEN situation_judiciaire_id = 'arrete' AND sexe = 'ذكر' THEN 1 ELSE 0 END),
    SUM(CASE WHEN situation_judiciaire_id = 'arrete' AND sexe = 'أنثى' THEN 1 ELSE 0 END),
    SUM(CASE WHEN situation_judiciaire_id = 'juge' AND sexe = 'ذكر' THEN 1 ELSE 0 END),
    SUM(CASE WHEN situation_judiciaire_id = 'juge' AND sexe = 'أنثى' THEN 1 ELSE 0 END),
    SUM(CASE 
            WHEN situation_judiciaire_id NOT IN ('arrete', 'juge') OR situation_judiciaire_id IS NULL 
            THEN 1 
            ELSE 0 
        END)
FROM ranked
WHERE rn = 1 

ORDER BY GOUFK NULLS LAST, arretemasculin DESC, arretefeminin DESC, jugemasculin DESC, jugefeminin DESC