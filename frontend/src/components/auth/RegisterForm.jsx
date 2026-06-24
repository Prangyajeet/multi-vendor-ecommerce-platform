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
          label="First Name"
          placeholder="Enter first name"
        />

        <Input
          label="Last Name"
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

      <div className="flex flex-col gap-2">
        <label>Role</label>

        <select className="border rounded-lg p-2">
        <option value="CUSTOMER">Customer</option>
        <option value="VENDOR">Vendor</option>
        <option value="ADMIN">Admin</option>
        </select>
      </div>

        <Button className="w-full">
          Register
        </Button>

      </div>

    </div>
  );
}

export default RegisterForm;