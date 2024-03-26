package io.determann.serialization.view;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class SettingImpl implements UniversalSetting
{
   private final String namespace;
   private final String name;
   private final Object value;

   public SettingImpl(String namespace, String name, Object value)
   {
      requireNonNull(namespace);
      if (namespace.isBlank())
      {
         throw new IllegalArgumentException("Namespace can not be blank");
      }
      requireNonNull(name);
      if (name.isBlank())
      {
         throw new IllegalArgumentException("Name can not be blank");
      }
      this.namespace = namespace;
      this.name = name;
      this.value = value;
   }

   @Override
   public String getNamespace()
   {
      return namespace;
   }

   public String getName()
   {
      return name;
   }

   public Object getValue()
   {
      return value;
   }

   @Override
   public boolean equals(Object other)
   {

      return other == null ||
             other instanceof SettingImpl otherSetting &&
             Objects.equals(getName(), otherSetting.getName()) &&
             Objects.equals(getValue(), otherSetting.getValue());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName(), getValue());
   }

   @Override
   public String toString()
   {
      return "Setting{" +
             "name='" + name + '\'' +
             ", value=" + value +
             '}';
   }
}
