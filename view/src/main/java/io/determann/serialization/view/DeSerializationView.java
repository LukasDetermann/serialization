package io.determann.serialization.view;

public sealed interface DeSerializationView<TYPE, SELF extends DeSerializationView<TYPE, SELF>> extends View<TYPE, SELF>
      permits ConstructorDeSerializationView,
              PropertyDeSerializationView,
              PropertyView
{
}
