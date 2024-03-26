package io.determann.serialization.view;

public non-sealed interface ReadAndWriteProperty<TYPE, PROPERTY, SELF extends ReadAndWriteProperty<TYPE, PROPERTY, SELF>>
      extends DeSerializationProperty<TYPE, PROPERTY, SELF>,
              SerializationProperty<TYPE, PROPERTY, SELF>
{
}