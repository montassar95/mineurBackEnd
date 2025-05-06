//package com.cgpr.mineur.controllers;
//
// 
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.cgpr.mineur.models.rapport.DetenuView;
//import com.cgpr.mineur.serviceReporting.DetenuViewService;
//
//@RestController
//@RequestMapping("/api/detenu-view")
//public class DetenuViewController {
//
//   
//
//    @Autowired
//    private DetenuViewService detenuViewService;
//
//   
//   
//    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<DetenuView>> findByCriteria(
//            @RequestParam(required = false) String nomEtablissement,
//         
//            @RequestParam(required = false) String decision,
//           
//       
//            @RequestParam(required = false) String classePenale,
//            @RequestParam(required = false) String typeAffaire,
//            @RequestParam(required = false) String typeTribunal,
//     
//            @RequestParam(required = false) String typeJugement,
//            @RequestParam(required = false) Integer ageMin,
//            @RequestParam(required = false) Integer ageMax, 
//            @RequestParam(required = false) String nationalite
//    ) {
//        List<DetenuView> result = detenuViewService.findByCriteria(
//                nomEtablissement,
//                
//                decision,
//               
//                
//                classePenale,
//                typeAffaire,
//                typeTribunal,
//                
//                typeJugement,
//                ageMin,
//                ageMax,
//                
//                nationalite
//        );
//        if (result.isEmpty()) {
//            return ResponseEntity.noContent().build(); // Retourne 204 si aucun résultat n'est trouvé
//        }
//        return ResponseEntity.ok(result); // Retourne un JSON avec les résultats
//    }
//}
