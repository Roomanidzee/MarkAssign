package io.vscale.uniservice.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 22.05.2018
 *
 * @author Dias Arkharov
 * @version 1.0
 */
@Component
public class FormDataParser {

    public void parse(Enumeration<String> parameters, HttpServletRequest request){
        while (parameters.hasMoreElements()){
            String parameter = parameters.nextElement();
            if (parseConfirmation(parameter)){
                boolean confirmation = request.getParameter(parameter).equals("true");
                Long id = parseId(parameter);
            }

            if (parseScoreForm(parameter)){
                String score = request.getParameter(parameter);
                Long id = parseId(parameter);
            }
        }
    }

    private boolean parseScoreForm(String string){
        return string.contains("addScoresForm");
    }

    private boolean parseConfirmation(String string){
        return string.contains("confirm");
    }

    private Long parseId(String string){
        return Long.parseLong(string.replaceAll("\\D+",""));
    }
}
