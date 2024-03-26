package io.determann.serialization.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class PropertyImpl<TYPE, PROPERTY, SELF extends ReadAndWriteProperty<TYPE, PROPERTY, SELF>>
      implements ReadAndWriteProperty<TYPE, PROPERTY, SELF>
{
   private final String name;
   private final Class<PROPERTY> type;
   private final BiConsumer<TYPE, PROPERTY> setter;
   private final Function<TYPE, PROPERTY> getter;

   private final DeSerializationView<PROPERTY, ?> nestedDeSerializationView;
   private final SerializationView<PROPERTY, ?> nestedSerializationView;

   private final List<PropertySetting> settings = new ArrayList<>();

   PropertyImpl(String name,
                Class<PROPERTY> type,
                Function<TYPE, PROPERTY> getter,
                BiConsumer<TYPE, PROPERTY> setter,
                DeSerializationView<PROPERTY, ?> nestedDeSerializationView,
                SerializationView<PROPERTY, ?> nestedSerializationView)
   {
      this.name = requireNonNull(name);
      this.type = requireNonNull(type);
      if (getter == null && setter == null)
      {
         throw new IllegalArgumentException("setter and getter are null");
      }
      this.setter = setter;
      this.getter = getter;
      this.nestedDeSerializationView = nestedDeSerializationView;
      this.nestedSerializationView = nestedSerializationView;
   }

   PropertyImpl(Property<TYPE, PROPERTY, ?> property)
   {
      requireNonNull(property);
      //noinspection unchecked
      PropertyImpl<TYPE, PROPERTY, ?> impl = (PropertyImpl<TYPE, PROPERTY, ?>) property;
      name = requireNonNull(impl.getName());
      type = requireNonNull(impl.getType());
      setter = impl.getSetter();
      getter = impl.getGetter();
      nestedDeSerializationView = impl.getNestedWrite().orElse(null);
      nestedSerializationView = impl.getNestedRead().orElse(null);
      settings.addAll(requireNonNull(impl.getSettings()));
   }

   @Override
   public String getName()
   {
      return name;
   }

   public Class<PROPERTY> getType()
   {
      return type;
   }

   @Override
   public SELF withSetting(PropertySetting setting)
   {
      //noinspection rawtypes,unchecked
      SELF copy = (SELF) new PropertyImpl(this);
      copy.getSettings().add(setting);
      return copy;
   }

   @Override
   public BiConsumer<TYPE, PROPERTY> getSetter()
   {
      return setter;
   }

   @Override
   public Function<TYPE, PROPERTY> getGetter()
   {
      return getter;
   }

   @Override
   public Optional<SerializationView<PROPERTY, ?>> getNestedRead()
   {
      return Optional.ofNullable(nestedSerializationView);
   }

   @Override
   public Optional<DeSerializationView<PROPERTY, ?>> getNestedWrite()
   {
      return Optional.ofNullable(nestedDeSerializationView);
   }

   @Override
   public List<PropertySetting> getSettings()
   {
      return settings;
   }

   @Override
   public boolean equals(Object other)
   {
      return other == null ||
             other instanceof PropertyImpl<?, ?, ?> otherproperty && Objects.equals(getName(), otherproperty.getName());
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName());
   }
}
