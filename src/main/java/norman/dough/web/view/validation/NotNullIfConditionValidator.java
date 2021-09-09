package norman.dough.web.view.validation;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotNullIfConditionValidator implements ConstraintValidator<NotNullIfCondition, Object> {
    private String fieldName;
    private String conditionField;

    @Override
    public void initialize(NotNullIfCondition constraintAnnotation) {
        fieldName = constraintAnnotation.fieldName();
        conditionField = constraintAnnotation.conditionField();
    }

    @Override
    public boolean isValid(Object formBean, ConstraintValidatorContext context) {
        boolean isValid = true;
        if (formBean != null) {
            boolean conditionValue = (boolean) new BeanWrapperImpl(formBean).getPropertyValue(conditionField);
            if (conditionValue) {
                Object fieldValue = new BeanWrapperImpl(formBean).getPropertyValue(fieldName);
                isValid = fieldValue != null;
            }
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            String message = context.getDefaultConstraintMessageTemplate();
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(fieldName).addConstraintViolation();
        }
        return isValid;
    }
}
