package norman.dough.web.view.validation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

public class AfterDateIfValueChangeValidator implements ConstraintValidator<AfterDateIfValueChange, Object> {
    private String newDate;
    private String oldDate;
    private String newString;
    private String oldString;

    @Override
    public void initialize(AfterDateIfValueChange constraintAnnotation) {
        newDate = constraintAnnotation.newDate();
        oldDate = constraintAnnotation.oldDate();
        newString = constraintAnnotation.newString();
        oldString = constraintAnnotation.oldString();
    }

    @Override
    public boolean isValid(Object formBean, ConstraintValidatorContext constraintValidatorContext) {
        Date newDateValue = (Date) new BeanWrapperImpl(formBean).getPropertyValue(newDate);
        Date oldDateValue = (Date) new BeanWrapperImpl(formBean).getPropertyValue(oldDate);
        String newStringValue = (String) new BeanWrapperImpl(formBean).getPropertyValue(newString);
        String oldStringValue = (String) new BeanWrapperImpl(formBean).getPropertyValue(oldString);
        // If old string value is blank, this is a new record and we don't care about the date.
        if (StringUtils.isBlank(oldStringValue)) {
            return true;
            // Otherwise, if string value is unchanged, date must be unchanged.
        } else if (oldStringValue.equals(newStringValue)) {
            return oldDateValue.equals(newDateValue);
            // Otherwise, string value has changed. New date must be after old date.
        } else {
            return newDateValue != null && oldDateValue.before(newDateValue);
        }
    }
}
