import Input from "../common/Input";
import Button from "../common/Button";

function RegisterForm() {
  return (
    <div className="bg-white p-8 rounded-xl shadow-md w-96">

      <h2 className="text-2xl font-bold mb-6">
        Register
      </h2>

      <div className="space-y-4">

        <Input
          label="Name"
          placeholder="Enter name"
        />

        <Input
          label="Email"
          placeholder="Enter email"
        />

        <Input
          label="Password"
          type="password"
          placeholder="Password"
        />

        <Input
          label="Confirm Password"
          type="password"
          placeholder="Confirm Password"
        />

        <Button className="w-full">
          Register
        </Button>

      </div>

    </div>
  );
}

export default RegisterForm;