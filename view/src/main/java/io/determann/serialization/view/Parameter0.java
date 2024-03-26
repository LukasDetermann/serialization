package io.determann.serialization.view;

public interface Parameter0<TYPE>
{
   <P1> Parameter1<TYPE, P1> parameter1(Parameter<TYPE, P1> parameter);

   ConstructorDeSerializationView<TYPE> constructor(Constructor0<TYPE> constructor);
}
