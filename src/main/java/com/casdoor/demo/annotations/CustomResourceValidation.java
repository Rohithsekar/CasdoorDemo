package com.casdoor.demo.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface CustomResourceValidation {
  Class<?> validator(); // Class containing the validation method
  String method(); // Name of the validation method
}