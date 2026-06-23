import Input from "../common/Input";
import Button from "../common/Button";

function LoginForm() {
  return (
    <div className="bg-white p-8 rounded-xl shadow-md w-96">

      <h2 className="text-2xl font-bold mb-6">
        Login
      </h2>

      <div className="space-y-4">

        <Input
          label="Email"
          placeholder="Enter email"
        />

        <Input
          label="Password"
          type="password"
          placeholder="Enter password"
        />

        <Button className="w-full">
          Login
        </Button>

      </div>

    </div>
  );
}

export default LoginForm;