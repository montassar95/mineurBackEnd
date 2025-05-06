 
 
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
    INNER JOIN (
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
        AND res.ETAFK = :etablissement_reside
    ON arr.id_enf = res.id_enf  
    AND arr.num_ord = res.NUM_ORD_ARR
    LEFT JOIN CAU_LIB cau_lib 
        ON lib.CAU_LIBFK = cau_lib.id  -- Jointure avec CAU_LIB
      WHERE 
     --  TRUNC(arr.date_arrestation) <= :dateFin AND 
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
),

-- CTE pour obtenir la situation judiciaire pour la période donnée
document_decision AS (
    SELECT 
        cte.id_enf, 
        cte.num_ord AS num_ord_arr,
        cte.num_ord_res AS num_ord_res,
        cte.date_arrestation,
       
        cte.statut,
        LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) AS documents_list,
        CASE
           
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
        CASE
           
            WHEN LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CD%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CH%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%AE%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%AP%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%T%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CJA%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CP%'
                 
                 THEN 'موقــــــــوف'
            WHEN LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CJ%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CR%' OR
                 LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%CRR%'
            THEN 'محكــــــــــوم'
            WHEN LISTAGG(doc.type_document, ', ') WITHIN GROUP (ORDER BY doc.num_ord_doc) LIKE '%ArretEx%'
            THEN 'لم يتم إدراج السراح'
            ELSE NULL
           
        END AS situation_judiciaire 
    FROM cte_statut cte
    LEFT JOIN last_document_by_aff ldba
        ON cte.id_enf = ldba.id_enf 
        AND cte.num_ord = ldba.num_ord_arr
    LEFT JOIN doc doc 
        ON ldba.id_enf = doc.id_enf 
        AND ldba.num_ord_arr = doc.num_ord_arr 
        AND ldba.num_ord_aff = doc.num_ord_aff 
        AND ldba.max_num_ord_doc_by_aff = doc.num_ord_doc_by_aff
    GROUP BY cte.id_enf, cte.num_ord, cte.statut, cte.num_ord_res ,date_arrestation 
),

-- CTE pour obtenir les informations détaillées sur les détenus
info_detenu AS (
    SELECT 
        document_decision.id_enf, 
        detenu.id AS id_detenue,
        detenu.nom || ' بن ' || detenu.nom_pere || ' بن ' || detenu.nom_grand_pere || ' ' || detenu.prenom AS nom_complet,
        detenu.DATE_NAISSANCE AS date_naissance,
        TO_CHAR(detenu.DATE_NAISSANCE, 'YYYY-MM-DD') || ' بــ' || detenu.lieu_naissance AS date_lieu_naissance,
        ADD_MONTHS(detenu.date_naissance, 18 * 12) AS date_majorite,
        TRUNC(MONTHS_BETWEEN(:dateFin , detenu.DATE_NAISSANCE) / 12) AS age,
        gouvernorat.id AS gouvernorat_adresse_id,
        delegation.id AS delegation_adresse_id,
        detenu.adresse || ' ' || gouvernorat.libelle_gouvernorat || ' ' || delegation.libelle_delegation AS adresse_complete,
        niveau_educatif.id AS niveau_educatif_id,
        metier.id AS metier_id,
        niveau_educatif.libelle_niveau_educatif || ' ' || metier.libelle_metier AS formation_metier,
        situation_familiale.id AS situation_familiale_id,
        situation_sociale.id AS situation_sociale_id,
        situation_familiale.libelle_situation_familiale || ' ' || situation_sociale.libelle_situation_social AS situation_familiale_sociale,
        nationalite.id AS nationalite_id,
        nationalite.libelle_nationalite AS nationalite,
        classe_penale.id AS classe_penale_id,
        classe_penale.libelle_classe_penale AS classe_penale,
        r.num_ord_arr AS numero_ord_arr,
        r.num_ord_res AS numero_ord_res,
        r.num_arrestation AS numero_arrestation,
        etablissement.id AS etablissement_reside_id,
        etablissement.libelle_etablissement AS etablissement_reside,
        CASE 
            WHEN etablissement_entree.libelle_etablissement IS NOT NULL 
            THEN ' نقل  من ' || etablissement_entree.libelle_etablissement || ' ل' || cause_mutation.LIBELLE_CAUSE_MUTATION || ' ' || TO_CHAR(r.DATE_ENTREE, 'DD-MM-YYYY')   
        END AS evenement_entree,
        etablissement_sortie.id as  evenement_mutaion_id,
        etablissement_sortie.libelle_etablissement AS  evenement_mutaion,
        document_decision.situation_judiciaire_id  ,
        document_decision.situation_judiciaire ,
        document_decision.date_arrestation
       
    FROM document_decision
    LEFT JOIN res r 
        ON r.id_enf = document_decision.id_enf 
        AND r.num_ord_arr = document_decision.num_ord_arr 
        AND r.num_ord_res = document_decision.num_ord_res
    LEFT JOIN enf detenu 
        ON r.id_enf = detenu.id
    LEFT JOIN eta etablissement 
        ON r.etafk = etablissement.id
    LEFT JOIN eta etablissement_entree 
        ON r.ETAFKE = etablissement_entree.id
    LEFT JOIN eta etablissement_sortie 
        ON r.ETAFKS = etablissement_sortie.id
    LEFT JOIN cau_mut cause_mutation 
        ON r.CAU_MUTFK = cause_mutation.id
    LEFT JOIN cla_pen classe_penale 
        ON detenu.classe_penale_id = classe_penale.id
    LEFT JOIN del delegation 
        ON detenu.delegation_id = delegation.id
    LEFT JOIN gou gouvernorat 
        ON detenu.gouvernorat_id = gouvernorat.id
    LEFT JOIN nat nationalite 
        ON detenu.nationalite_id = nationalite.id
    LEFT JOIN niv_edu niveau_educatif 
        ON detenu.niveau_educatif_id = niveau_educatif.id
    LEFT JOIN sit_fam situation_familiale 
        ON detenu.situation_familiale_id = situation_familiale.id
    LEFT JOIN met metier 
        ON detenu.metier_id = metier.id
    LEFT JOIN sit_soc situation_sociale 
        ON detenu.situation_social_id = situation_sociale.id
   where situation_judiciaire_id = :situation_judiciaire_id     
),

-- CTE pour obtenir le dernier document associé par arrestation pour une période donnée
last_document_associe AS (
    SELECT doc.id_enf, doc.num_ord_arr, doc.num_ord_aff, 
           MAX(doc.num_ord_doc) AS max_num_ord_doc 
    FROM info_detenu cte
    LEFT JOIN doc doc 
    ON cte.id_detenue = doc.id_enf
    AND cte.numero_ord_arr = doc.num_ord_arr
    AND TRUNC(doc.date_emission) <= :dateFin 
    AND doc.type_document IN ('CJ', 'CJA', 'CD', 'CH') 
    AND doc.type_document_actuelle IS NULL
    GROUP BY doc.id_enf, doc.num_ord_arr, doc.num_ord_aff    
),

-- CTE pour obtenir le dernier document associé par arrestation et affaire pour une période donnée
last_document_by_aff_associe AS (
    SELECT ld.id_enf, 
           ld.num_ord_arr, 
           ld.num_ord_aff,
           ld.max_num_ord_doc,
           MAX(doc.num_ord_doc_by_aff) AS max_num_ord_doc_by_aff  
    FROM last_document_associe ld
    LEFT JOIN doc doc 
    ON ld.id_enf = doc.id_enf 
    AND ld.num_ord_arr = doc.num_ord_arr 
    AND ld.num_ord_aff = doc.num_ord_aff 
    AND ld.max_num_ord_doc = doc.num_ord_doc
    AND TRUNC(doc.date_emission) <= :dateFin 
    AND doc.type_document IN ('CJ', 'CJA', 'CD', 'CH') 
    AND doc.type_document_actuelle IS NULL
    GROUP BY ld.id_enf, ld.num_ord_arr, ld.num_ord_aff, ld.max_num_ord_doc
),

-- CTE pour obtenir le dernier document par arrestation pour une période donnée
last_document_dernier AS (
    SELECT doc.id_enf, doc.num_ord_arr, doc.num_ord_aff, 
           MAX(doc.num_ord_doc) AS max_num_ord_doc 
    FROM info_detenu cte
    LEFT JOIN doc doc 
    ON cte.id_detenue = doc.id_enf
    AND cte.numero_ord_arr = doc.num_ord_arr
    AND TRUNC(doc.date_emission) <= :dateFin 
    GROUP BY doc.id_enf, doc.num_ord_arr, doc.num_ord_aff    
),

-- CTE pour obtenir le dernier document par arrestation et affaire pour une période donnée
last_document_by_aff_dernier AS (
    SELECT ld.id_enf, 
           ld.num_ord_arr, 
           ld.num_ord_aff,
           ld.max_num_ord_doc,
           MAX(doc.num_ord_doc_by_aff) AS max_num_ord_doc_by_aff  
    FROM last_document_dernier ld
    LEFT JOIN doc doc 
    ON ld.id_enf = doc.id_enf 
    AND ld.num_ord_arr = doc.num_ord_arr 
    AND ld.num_ord_aff = doc.num_ord_aff 
    AND ld.max_num_ord_doc = doc.num_ord_doc
    AND TRUNC(doc.date_emission) <= :dateFin 
    GROUP BY ld.id_enf, ld.num_ord_arr, ld.num_ord_aff, ld.max_num_ord_doc
),

-- CTE pour obtenir le document global
last_document_globale AS (
    SELECT 
        COALESCE(da.a_id_enf, dd.a_id_enf) AS a_id_enf,
        COALESCE(da.a_num_ord_arr, dd.a_num_ord_arr) AS a_num_ord_arr,
        COALESCE(da.num_ord_aff, dd.num_ord_aff) AS num_ord_aff,
        da.num_ord_doc AS num_ord_doc_associe,
        da.num_ord_doc_by_aff AS num_ord_doc_by_aff_associe,
        dd.num_ord_doc AS num_ord_doc_dernier,
        dd.num_ord_doc_by_aff AS num_ord_doc_by_aff_dernier
    FROM last_document_by_aff_associe lda
    LEFT JOIN doc da 
        ON da.a_id_enf = lda.id_enf
        AND da.a_num_ord_arr = lda.num_ord_arr
        AND da.num_ord_aff = lda.num_ord_aff
        AND da.num_ord_doc = lda.max_num_ord_doc
        AND da.num_ord_doc_by_aff = lda.max_num_ord_doc_by_aff
    FULL OUTER JOIN last_document_by_aff_dernier ldd
        ON lda.id_enf = ldd.id_enf
        AND lda.num_ord_arr = ldd.num_ord_arr
        AND lda.num_ord_aff = ldd.num_ord_aff
    LEFT JOIN doc dd 
        ON dd.a_id_enf = ldd.id_enf
        AND dd.a_num_ord_arr = ldd.num_ord_arr
        AND dd.num_ord_aff = ldd.num_ord_aff
        AND dd.num_ord_doc = ldd.max_num_ord_doc
        AND dd.num_ord_doc_by_aff = ldd.max_num_ord_doc_by_aff
),

-- Sélection finale des données
synthese as (
SELECT  
   vm.id_detenue,
    vm.nom_complet,
    vm.date_naissance,
    vm.date_lieu_naissance,
    vm.date_majorite,
    vm.age,
    vm.gouvernorat_adresse_id,
    vm.delegation_adresse_id,
    vm.adresse_complete,
    vm.niveau_educatif_id,
    vm.metier_id,
    vm.formation_metier,
    vm.situation_familiale_id,
    vm.situation_sociale_id,
    vm.situation_familiale_sociale,
    vm.nationalite_id,
    vm.nationalite,
    vm.classe_penale_id,
    vm.classe_penale,
    vm.numero_ord_arr,
    vm.numero_ord_res,
    vm.numero_arrestation,
    vm.etablissement_reside_id,
    vm.etablissement_reside,
    vm.evenement_entree,
    vm.evenement_mutaion_id,
    vm.evenement_mutaion,
    vm.situation_judiciaire_id,
     vm.situation_judiciaire,
     vm.date_arrestation,
    
      dernier_document.a_num_aff as NUMERO_AFFAIRE , 
    gouvernorat_tribunal.id AS gouvernorat_tribunal_id,
    type_tribunal.id AS type_tribunal_id,
    
    tribunal.nom_tribunal AS nom_tribunal,
     type_affaire.id AS type_affaire_id,
    type_affaire.libelle_type_affaire AS type_affaire,
     car_rec.DATE_DEBUT_PUNITION AS DATE_DEBUT_PUNITION_AFFAIRE,    
    car_rec.DATE_FIN_PUNITION AS DATE_FIN_PUNITION_AFFAIRE,
  -- Ajout des nouvelles colonnes avec PARTITION BY id_detenue et numero_ord_arr
MIN(
    CASE 
        WHEN car_rec.days_diff_juge > 0 THEN car_rec.DATE_DEBUT_PUNITION 
        ELSE NULL 
    END
) OVER (PARTITION BY vm.id_detenue, vm.numero_ord_arr) AS DATE_DEBUT_PUNITION_Globale,
    MAX(car_rec.DATE_FIN_PUNITION) OVER (PARTITION BY vm.id_detenue, vm.numero_ord_arr) AS DATE_FIN_PUNITION_GLOBALE,
    dernier_document.type_document AS type_document_dernier,
    CASE 
        WHEN dernier_document.DOCUMENT_TYPE = 'ChangementLieu' THEN 'قرار تغير مكان الإيداع'  
        WHEN dernier_document.type_document = 'CD' THEN 'بطاقة إيداع'  
        WHEN dernier_document.type_document = 'CH' THEN 'بطاقة إيواء'  
        WHEN dernier_document.type_document = 'ArretEx' THEN 'إيقاف تنفيذ الحكم ســــــــــــراح'  
        WHEN dernier_document.type_document IN ('CJ', 'CJA') THEN 'مضمون حكم'  
        WHEN dernier_document.type_document = 'T' THEN 'إحالة'  
        WHEN dernier_document.type_document = 'AE' THEN 'استئناف الطفل'  
        WHEN dernier_document.type_document = 'AP' THEN 'استئناف النيابة'  
        WHEN dernier_document.type_document = 'CR' THEN 'مراجعة'  
        WHEN dernier_document.type_document = 'CRR' THEN 'رفض المراجعة'  
        WHEN dernier_document.type_document = 'CP' THEN 'قرار تمديد'  
 
        ELSE 'نوع مستند غير معروف'  
    END AS type_document_dernier_alias,
    dernier_document.DATE_EMISSION AS date_emission_document_dernier,
    document_associe.type_document AS type_document_associe,
    CASE 
        WHEN document_associe.DOCUMENT_TYPE = 'ChangementLieu' THEN 'قرار تغير مكان الإيداع' 
        WHEN document_associe.type_document = 'CD' THEN 'بطاقة إيداع'
        WHEN document_associe.type_document = 'CH' THEN 'بطاقة إيواء'
        WHEN document_associe.type_document = 'ArretEx' THEN 'إيقاف تنفيذ الحكم ســــــــــــاح'
        WHEN document_associe.type_document IN ('CJ', 'CJA') THEN 'مضمون حكم'
        WHEN document_associe.type_document = 'T' THEN 'إحالة'
        WHEN document_associe.type_document = 'AE' THEN 'استئناف الطفل'
        WHEN document_associe.type_document = 'AP' THEN 'استئناف النيابة'
        WHEN document_associe.type_document = 'CR' THEN 'مراجعة'
        WHEN document_associe.type_document = 'CRR' THEN 'رفض المراجعة'
        WHEN document_associe.type_document = 'CP' THEN 'قرار تمديد'
     
        ELSE 'نوع مستند غير معروف'
    END AS type_document_associe_alias,
    document_associe.DATE_EMISSION AS date_emission_document_associe,
    typ_jug.LIBELLE_TYPE_JUGE AS TYPE_JUGE,
   typ_jug.id  AS TYPE_JUGE_ID,
    car_rec.jour AS jour_punition,
    car_rec.mois AS mois_punition,
    car_rec.annee AS annee_punition,
    CASE 
        WHEN document_associe.type_document = 'CD' THEN accusations_depot.accusations
        WHEN document_associe.type_document = 'CH' THEN accusations_hebergement.accusations
        WHEN document_associe.type_document IN ('CJ', 'CJA') THEN accusations_recuperation.accusations
        ELSE NULL
    END AS accusations,
    CASE 
        WHEN document_associe.type_document = 'CD' THEN accusations_depot.accusations_id
        WHEN document_associe.type_document = 'CH' THEN accusations_hebergement.accusations_id
        WHEN document_associe.type_document IN ('CJ', 'CJA') THEN accusations_recuperation.accusations_id
        ELSE NULL
    END AS accusations_id,
     visite.NBR_VISITE
    
   
FROM last_document_globale last_document_globale
LEFT JOIN info_detenu vm 
    ON last_document_globale.a_id_enf = vm.id_detenue 
    AND last_document_globale.a_num_ord_arr = vm.numero_ord_arr 
LEFT JOIN doc dernier_document 
    ON last_document_globale.a_id_enf = dernier_document.id_enf 
    AND last_document_globale.a_num_ord_arr = dernier_document.num_ord_arr 
    AND last_document_globale.num_ord_aff = dernier_document.num_ord_aff
    AND last_document_globale.num_ord_doc_dernier = dernier_document.num_ord_doc
    AND last_document_globale.num_ord_doc_by_aff_dernier = dernier_document.num_ord_doc_by_aff
LEFT JOIN tri tribunal ON dernier_document.a_id_tri = tribunal.id
LEFT JOIN gou gouvernorat_tribunal ON tribunal.GOUFK = gouvernorat_tribunal.id
LEFT JOIN typ_tri type_tribunal ON tribunal.TYP_TRIFK = type_tribunal.id
LEFT JOIN doc document_associe 
    ON last_document_globale.a_id_enf = document_associe.id_enf 
    AND last_document_globale.a_num_ord_arr = document_associe.num_ord_arr 
    AND last_document_globale.num_ord_aff = document_associe.num_ord_aff
    AND last_document_globale.num_ord_doc_associe = document_associe.num_ord_doc
    AND last_document_globale.num_ord_doc_by_aff_associe = document_associe.num_ord_doc_by_aff  
LEFT JOIN (
    SELECT DISTINCT 
        CARTE_DEPOT_id_enf,
        CARTE_DEPOT_num_ord_arr,
        CARTE_DEPOT_num_ord_aff,
        CARTE_DEPOT_num_ord_doc,
        CARTE_DEPOT_NUM_ORD_DOC_BY_AFF,
        LISTAGG(tit.titre_accusation, ' و ') WITHIN GROUP (ORDER BY TITRE_ACCUSATIONFK) AS accusations,
        LISTAGG(tit.id, ' , ') WITHIN GROUP (ORDER BY TITRE_ACCUSATIONFK) AS accusations_id
    FROM ACC_CAR_DEP
    INNER JOIN tit_acc tit ON ACC_CAR_DEP.TITRE_ACCUSATIONFK = tit.id
    GROUP BY CARTE_DEPOT_id_enf, CARTE_DEPOT_num_ord_arr, CARTE_DEPOT_num_ord_aff, CARTE_DEPOT_num_ord_doc, CARTE_DEPOT_NUM_ORD_DOC_BY_AFF
) accusations_depot ON document_associe.id_enf = accusations_depot.CARTE_DEPOT_id_enf AND document_associe.num_ord_arr = accusations_depot.CARTE_DEPOT_num_ord_arr AND document_associe.num_ord_aff = accusations_depot.CARTE_DEPOT_num_ord_aff AND document_associe.num_ord_doc = accusations_depot.CARTE_DEPOT_num_ord_doc AND document_associe.NUM_ORD_DOC_BY_AFF = accusations_depot.CARTE_DEPOT_NUM_ORD_DOC_BY_AFF
LEFT JOIN (
    SELECT DISTINCT 
        CARTE_HEBER_id_enf,
        CARTE_HEBER_num_ord_arr,
        CARTE_HEBER_num_ord_aff,
        CARTE_HEBER_num_ord_doc,
        CARTE_HEBER_NUM_ORD_DOC_BY_AFF,
        LISTAGG(tit.titre_accusation, ' و ') WITHIN GROUP (ORDER BY TITRE_ACC_HEBFK) AS accusations,
        LISTAGG(tit.id, ' , ') WITHIN GROUP (ORDER BY TITRE_ACC_HEBFK) AS accusations_id
    FROM ACC_CAR_HEB
    INNER JOIN tit_acc tit ON ACC_CAR_HEB.TITRE_ACC_HEBFK = tit.id
    GROUP BY CARTE_HEBER_id_enf, CARTE_HEBER_num_ord_arr, CARTE_HEBER_num_ord_aff, CARTE_HEBER_num_ord_doc, CARTE_HEBER_NUM_ORD_DOC_BY_AFF
) accusations_hebergement ON document_associe.id_enf = accusations_hebergement.CARTE_HEBER_id_enf AND document_associe.num_ord_arr = accusations_hebergement.CARTE_HEBER_num_ord_arr AND document_associe.num_ord_aff = accusations_hebergement.CARTE_HEBER_num_ord_aff AND document_associe.num_ord_doc = accusations_hebergement.CARTE_HEBER_num_ord_doc AND document_associe.NUM_ORD_DOC_BY_AFF = accusations_hebergement.CARTE_HEBER_NUM_ORD_DOC_BY_AFF
LEFT JOIN (
    SELECT DISTINCT 
        CARTE_RECUP_id_enf,
        CARTE_RECUP_num_ord_arr,
        CARTE_RECUP_num_ord_aff,
        CARTE_RECUP_num_ord_doc,
        CARTE_RECUP_NUM_ORD_DOC_BY_AFF,
        LISTAGG(tit.titre_accusation, ' و ') WITHIN GROUP (ORDER BY TITRE_ACCUSATIONFK) AS accusations,
        LISTAGG(tit.id, ' , ') WITHIN GROUP (ORDER BY TITRE_ACCUSATIONFK) AS accusations_id
    FROM ACC_CAR_REC 
    INNER JOIN tit_acc tit ON ACC_CAR_REC.TITRE_ACCUSATIONFK = tit.id
    GROUP BY CARTE_RECUP_id_enf, CARTE_RECUP_num_ord_arr, CARTE_RECUP_num_ord_aff, CARTE_RECUP_num_ord_doc, CARTE_RECUP_NUM_ORD_DOC_BY_AFF
) accusations_recuperation ON document_associe.id_enf = accusations_recuperation.CARTE_RECUP_id_enf AND document_associe.num_ord_arr = accusations_recuperation.CARTE_RECUP_num_ord_arr AND document_associe.num_ord_aff = accusations_recuperation.CARTE_RECUP_num_ord_aff AND document_associe.num_ord_doc = accusations_recuperation.CARTE_RECUP_num_ord_doc AND document_associe.NUM_ORD_DOC_BY_AFF = accusations_recuperation.CARTE_RECUP_NUM_ORD_DOC_BY_AFF
LEFT JOIN typ_aff type_affaire ON document_associe.TYPFK = type_affaire.id
LEFT JOIN car_rec car_rec ON document_associe.id_enf = car_rec.id_enf 
                            AND document_associe.num_ord_arr = car_rec.num_ord_arr
                            AND document_associe.num_ord_aff = car_rec.num_ord_aff 
                            AND document_associe.num_ord_doc = car_rec.num_ord_doc 
                            AND document_associe.NUM_ORD_DOC_BY_AFF = car_rec.NUM_ORD_DOC_BY_AFF
LEFT JOIN typ_jug typ_jug ON car_rec.TYP_JUGFK = typ_jug.id

LEFT JOIN visite visite ON visite.R_ID_ENF_V =  vm.id_detenue
                        AND  visite.R_NUM_ORD_ARR_V =  vm.numero_ord_arr
                        AND  visite.R_NUM_ORD_RESI_V = vm.numero_ord_res
                        AND  TO_DATE(ANNEE_VISITE || '-' || MOIS_VISITE, 'YYYY-MM') 
                       BETWEEN :dateDebut  
                       AND :dateFin 
                       
)


 SELECT   id_detenue,
          nom_complet,
          date_lieu_naissance,
          nationalite,
          formation_metier,
         situation_familiale_sociale,
         adresse_complete,
         classe_penale,
         situation_judiciaire ,
         numero_arrestation,
         TO_CHAR( date_arrestation , 'YYYY-MM-DD'),
         DATE_DEBUT_PUNITION_Globale,
         DATE_FIN_PUNITION_Globale,
         etablissement_reside,
         evenement_entree,
         TO_CHAR(age) AS age,
         type_affaire,
         NUMERO_AFFAIRE,
        nom_tribunal,
        accusations,
      
        
         
      CASE 
    WHEN DATE_DEBUT_PUNITION_AFFAIRE IS NOT NULL 
    THEN 'بداية العقاب ' || TO_CHAR(DATE_DEBUT_PUNITION_AFFAIRE, 'DD-MM-YYYY') 
    ELSE ' ' 
END AS DEBUT_PUNITION_AFFAIRE,

CASE 
    WHEN DATE_FIN_PUNITION_AFFAIRE IS NOT NULL 
    THEN 'نهاية العقاب ' || TO_CHAR(DATE_FIN_PUNITION_AFFAIRE, 'DD-MM-YYYY') 
    ELSE ' ' 
END AS FIN_PUNITION_AFFAIRE,

        type_document_associe_alias || ' ' || TO_CHAR(date_emission_document_associe, 'DD-MM-YYYY')   as document_associe,
        type_document_dernier_alias || ' ' || TO_CHAR(date_emission_document_dernier, 'DD-MM-YYYY')    as document_dernier,
        
        CASE 
    WHEN TYPE_JUGE IS NOT NULL 
    THEN ' الحكم :' || ' ' || append_time_unit(jour_punition,mois_punition, annee_punition) || ' ' || TYPE_JUGE 
    ELSE ' ' 
END AS punition ,

TO_CHAR(NVL(NBR_VISITE, 0)) AS NBR_VISITE
 
     
    FROM synthese    order by id_detenue   
  