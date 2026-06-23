function Input({
  label,
  type = "text",
  placeholder,
}) {
  return (
    <div className="flex flex-col gap-2">
      <label>{label}</label>

      <input
        type={type}
        placeholder={placeholder}
        className="border rounded-lg p-2"
      />
    </div>
  );
}

export default Input;