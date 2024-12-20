package kisung.moin.common.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import kisung.moin.common.code.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드 제외
public class BasicResponse {
  @Schema(description = "응답 코드 (예: 200, 201)", example = "200")
  private int resultCode;

  @Schema(description = "응답 메시지", example = "SUCCESS")
  private String resultMsg;

  private Map<String, Object> additionalFields = new HashMap<>();

  // 동적으로 추가되는 필드 처리
  @JsonAnyGetter
  public Map<String, Object> getAdditionalFields() {
    return additionalFields;
  }

  // 동적으로 추가할 필드를 설정
  public void addField(String key, Object value) {
    additionalFields.put(key, value);
  }

  // 생성자 (기본 응답)
  public BasicResponse(SuccessCode successCode) {
    this.resultCode = successCode.getStatus();
    this.resultMsg = successCode.getMessage();
  }

  // 정적 메서드로 성공 응답 생성
  public static BasicResponse success(SuccessCode successCode) {
    return new BasicResponse(successCode);
  }
}