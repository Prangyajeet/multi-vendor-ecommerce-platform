import { useState } from "react";
import Input from "../common/Input";
import Button from "../common/Button";
import { registerUser } from "../../services/AuthService";

function RegisterForm() {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    confirmPassword: "",
    role: "CUSTOMER",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleRegister = async (e) => {
    e.preventDefault();

    if (formData.password !== formData.confirmPassword) {
      alert("Passwords do not match");
      return;
    }

    const requestBody = {
      firstName: formData.firstName,
      lastName: formData.lastName,
      email: formData.email,
      password: formData.password,
      role: formData.role,
    };

    try {
      const response = await registerUser(requestBody);

      alert(response.data.message);

      setFormData({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        confirmPassword: "",
        role: "CUSTOMER",
      });

    } catch (error) {
  console.error("Registration Error:", error);

  if (error.response) {
    console.log("Status:", error.response.status);
    console.log("Response:", error.response.data);

    alert(JSON.stringify(error.response.data));
  } else {
    alert(error.message);
  }
}
  };

  return (
    <form
      onSubmit={handleRegister}
      className="bg-white p-8 rounded-xl shadow-md w-96"
    >
      <h2 className="text-2xl font-bold mb-6">
        Register
      </h2>

      <div className="space-y-4">

        <Input
          label="First Name"
          name="firstName"
          placeholder="Enter first name"
          value={formData.firstName}
          onChange={handleChange}
        />

        <Input
          label="Last Name"
          name="lastName"
          placeholder="Enter last name"
          value={formData.lastName}
          onChange={handleChange}
        />

        <Input
          label="Email"
          name="email"
          placeholder="Enter email"
          value={formData.email}
          onChange={handleChange}
        />

        <Input
          label="Password"
          name="password"
          type="password"
          placeholder="Enter password"
          value={formData.password}
          onChange={handleChange}
        />

        <Input
          label="Confirm Password"
          name="confirmPassword"
          type="password"
          placeholder="Confirm password"
          value={formData.confirmPassword}
          onChange={handleChange}
        />

        <div className="flex flex-col gap-2">
          <label>Role</label>

          <select
            name="role"
            value={formData.role}
            onChange={handleChange}
            className="border rounded-lg p-2"
          >
            <option value="CUSTOMER">Customer</option>
            <option value="VENDOR">Vendor</option>
            <option value="ADMIN">Admin</option>
          </select>
        </div>

        <Button
          type="submit"
          className="w-full"
        >
          Register
        </Button>

      </div>
    </form>
  );
}

export default RegisterForm;