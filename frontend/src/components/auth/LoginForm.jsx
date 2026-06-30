import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Input from "../common/Input";
import Button from "../common/Button";
import { loginUser } from "../../services/AuthService";

function LoginForm() {

  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    });
  };

  const handleLogin = async (event) => {
    event.preventDefault();

    try {

      const response = await loginUser(formData);

      const { token, userId, email, role } = response.data;

      localStorage.setItem("token", token);
      localStorage.setItem("userId", userId);
      localStorage.setItem("email", email);
      localStorage.setItem("role", role);

      alert(response.data.message);

      switch (role) {

        case "ADMIN":
          navigate("/admin/dashboard");
          break;

        case "VENDOR":
          navigate("/vendor/dashboard");
          break;

        case "CUSTOMER":
          navigate("/customer/home");
          break;

        default:
          navigate("/login");
      }

    } catch (error) {

      if (error.response) {
        alert(error.response.data);
      } else {
        alert("Login Failed");
      }

    }
  };

  return (

    <form
      onSubmit={handleLogin}
      className="bg-white p-8 rounded-xl shadow-md w-96"
    >

      <h2 className="text-2xl font-bold mb-6">
        Login
      </h2>

      <div className="space-y-4">

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

        <Button
          type="submit"
          className="w-full"
        >
          Login
        </Button>

      </div>

    </form>

  );
}

export default LoginForm;