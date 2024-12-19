package kisung.moin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AESUtil {
  private final SecretKeySpec AES_KEY_SPEC;
  private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";

  public AESUtil(@Value("${aes.secret}") String secretKey) {
    if (secretKey.length() != 32) {
      throw new IllegalArgumentException("AES secret key must be 32 characters long");
    }
    this.AES_KEY_SPEC = new SecretKeySpec(secretKey.getBytes(), "AES");
  }

  public String encrypt(String data) throws Exception {
    Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

    byte[] iv = generateIV();
    IvParameterSpec ivSpec = new IvParameterSpec(iv);

    cipher.init(Cipher.ENCRYPT_MODE, AES_KEY_SPEC, ivSpec);
    byte[] encryptedData = cipher.doFinal(data.getBytes());

    String ivBase64 = Base64.getEncoder().encodeToString(iv);
    String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedData);
    return ivBase64 + ":" + encryptedBase64;
  }

  public String decrypt(String encryptedData) throws Exception {
    String[] parts = encryptedData.split(":");
    byte[] iv = Base64.getDecoder().decode(parts[0]);
    byte[] encryptedBytes = Base64.getDecoder().decode(parts[1]);

    Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
    IvParameterSpec ivSpec = new IvParameterSpec(iv);

    cipher.init(Cipher.DECRYPT_MODE, AES_KEY_SPEC, ivSpec);
    byte[] decryptedData = cipher.doFinal(encryptedBytes);
    return new String(decryptedData);
  }

  private byte[] generateIV() {
    byte[] iv = new byte[16];
    new SecureRandom().nextBytes(iv);
    return iv;
  }
}