WITH stat AS (  
 SELECT 
       enf.id,
        enf.sexe,
        nationalite.libelle_nationalite AS nationalite
    
    
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
   LEFT JOIN enf enf ON enf.id = arr.id_enf 
    LEFT JOIN nat nationalite ON enf.nationalite_id = nationalite.id
      WHERE 
      TRUNC(res.date_entree) <=  :dateFin  AND 
       ( TRUNC(lib.date_liberation) >  :dateFin    OR lib.date_liberation IS NULL)
)

SELECT 
    COALESCE(nationalite, 'المجموع') AS nationalite,
    SUM(CASE WHEN sexe = 'ذكر' OR sexe = '1' THEN 1 ELSE 0 END) AS nbr_hommes,
    SUM(CASE WHEN sexe = 'أنثى' OR sexe = '0' THEN 1 ELSE 0 END) AS nbr_femmes
FROM stat  
 
GROUP BY ROLLUP(nationalite)  
ORDER BY CASE WHEN nationalite = 'المجموع' THEN 1 ELSE 0 END, nationalite