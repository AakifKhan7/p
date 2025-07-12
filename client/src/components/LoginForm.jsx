import { Mail, Lock } from "lucide-react";
import { Link } from "react-router-dom";
import InputField from "./InputField";

const LoginForm = () => {
  return (
    <form className="w-full max-w-md flex flex-col gap-6">
      <InputField
        label="Email address"
        type="email"
        icon={Mail}
        placeholder="Enter your email"
      />
      <InputField
        label="Password"
        type="password"
        icon={Lock}
        placeholder="Enter your password"
      />

      <div className="flex items-center justify-between text-xs text-gray-600">
        <div className="flex items-center gap-2">
          <input type="checkbox" id="remember" className="accent-yellow-500" />
          <label htmlFor="remember" className="select-none">Remember me</label>
        </div>
        <Link to="#" className="text-yellow-600 font-medium hover:underline">Forgot password?</Link>
      </div>

      <button
        type="submit"
        className="mt-1 bg-yellow-500 hover:bg-yellow-600 text-black font-semibold py-2 rounded-md transition-colors"
      >
        Sign in
      </button>

      <p className="text-center text-xs text-gray-600">
        Don't have an account? {" "}
        <Link to="/signup" className="text-yellow-600 font-medium hover:underline">Create account</Link>
      </p>
    </form>
  );
};

export default LoginForm;
