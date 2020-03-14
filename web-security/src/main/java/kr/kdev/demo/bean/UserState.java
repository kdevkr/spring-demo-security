package kr.kdev.demo.bean;

import lombok.Data;

import java.util.Locale;
import java.util.TimeZone;

@Data
public class UserState {
    private String userId = "";
    private String name = "";
    private String role = "";
    private TimeZone timeZone = TimeZone.getDefault();
    private Locale locale = Locale.getDefault();
    private String sessionId = "";
}
