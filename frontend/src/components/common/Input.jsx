function Input({
  label,
  name,
  type = "text",
  placeholder,
  value,
  onChange,
}) {
  return (
    <div className="flex flex-col gap-2">
      <label>{label}</label>

      <input
        name={name}
        type={type}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        className="border rounded-lg p-2"
      />
    </div>
  );
}

export default Input;