package org.telluriumsource.ddt.object.mapping.validator

import org.telluriumsource.ddt.object.mapping.FieldSet
import org.telluriumsource.ddt.object.mapping.Field
import org.telluriumsource.ddt.object.mapping.DataMappingException
import org.telluriumsource.i18n.InternationalizationManager;
import org.telluriumsource.i18n.InternationalizationManagerImpl;


import java.util.regex.Pattern
import java.util.regex.Matcher

/**
 * Validate the field set reading from the file
 *
 * @author: Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Jul 25, 2008
 *
 */
class FieldSetValidator {
    protected static InternationalizationManager i18nManager = new InternationalizationManagerImpl();


    public static boolean validate(FieldSet fs, List fields){

        if(fs == null || fs.getFields() == null || fs.getFields().size() < 1)
            throw new DataMappingException(i18nManager.translate("FieldSetValidator.InvalidFieldSet"))

        int size = fields.size()
        
        if(fields == null || fs == null || fs.getFieldSize() != size)
            throw new DataMappingException(i18nManager.translate("FieldSetValidator.FieldSetSizeDoesNotMatch"))

        for (int i = 0; i < size; i++) {
            Field df = fs.getFields().get(i)
            String f = fields.get(i)
            
            //check if the field is empty
            if (f.length() == 0) {
                //useString the null value to replace the empty field
                if (df.getNullValue() != null)
                    f = df.getNullValue()
            }
            //check field length, if the length is specified, need to keep the last "LENGTH" characters
            if (df.getLength() > 0 && df.getLength() < f.length()) {
                f = f.substring(f.length() - df.getLength())
            }

            //Check if the field can be null
            if (f.length() == 0 && (!df.isNullable()))
                throw new DataMappingException(i18nManager.translate("FieldSetValidator.FieldCannotBeNull" , df.getName()))

            //only check the pattern if the field is not empty
            if (df.getPattern() != null && f.length() > 0) {
                //TODO:For efficient, we may need to move the pattern to data field class so that we do not need
                //to compile it each time.
                Pattern pattern = Pattern.compile(df.getPattern())
                Matcher matcher = pattern.matcher(f)
                //if not match
                if (!matcher.matches())
                    throw new DataMappingException(i18nManager.translate("FieldSetValidator.FieldDoesNotMatchPattern" , df.getPattern() ,df.getName()))
            }

            //If custom validator is defined and the field is not empty, we need to call it
//			if(df.getValidator() != null && f.length() > 0){
//			    if(!df.getValidator().validate(f))
//					throw new DataMappingException(ERROR_INVALID_FIELD_VALUE + f);
//			}

            return true
        }

    }
}