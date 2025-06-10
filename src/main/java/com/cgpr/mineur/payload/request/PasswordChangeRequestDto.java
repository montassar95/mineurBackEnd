package com.cgpr.mineur.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PasswordChangeRequestDto {
    private String oldPassword;
  

//    @NotBlank
//    @Size(min = 8, max = 40)
//    @Pattern(
//        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,40}$",
//        message = "يجب أن تحتوي كلمة المرور على بين 8 و 40 حرفًا، بما في ذلك حرف كبير، حرف صغير، رقم، وحرف خاص."
//    )
//    private String newPassword;
    
    @NotBlank
    @Size(min = 8, max = 40)
    @Pattern(
    	    regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d@#$%^&+=!]{8,40}$",
    	    message = "يجب أن تحتوي كلمة المرور على أحرف (كبيرة أو صغيرة) ورقم واحد على الأقل، ويمكن أن تحتوي على رموز خاصة، وطولها بين 8 و40 حرفًا."
    	)
    private String newPassword;

}
