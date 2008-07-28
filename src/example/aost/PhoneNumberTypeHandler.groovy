package example.aost

import java.text.SimpleDateFormat
import org.aost.datadriven.object.mapping.type.TypeHandler

/**
 * Example type handler to illustrate how to add custom type handler
 * 
 * @author: Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Jul 24, 2008
 *
 */
class PhoneNumberTypeHandler implements TypeHandler{
	protected final static String PHONE_SEPARATOR = "-"
	protected final static int PHONE_LENGTH = 12

    //remove the "-" inside the phone number
    public String valueOf(String s) {
		String value

		if(s != null && (!s.isEmpty())){
			 value = s.replaceAll(PHONE_SEPARATOR, "")
		}else {
			value = s
		}

		return value
    }


}