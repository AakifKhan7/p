import { Mail, Lock, User } from "lucide-react";
import InputField from "./InputField";

const SignupForm = () => {
  return (
    <form className="w-full max-w-md flex flex-col gap-6">
      <InputField
        label="Full name"
        type="text"
        icon={User}
        placeholder="Enter your full name"
      />
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
        placeholder="Create a password"
      />
      <InputField
        label="Confirm password"
        type="password"
        icon={Lock}
        placeholder="Confirm your password"
      />

      <div className="flex items-start gap-2 text-xs text-gray-600">
        <input type="checkbox" id="terms" className="mt-1 accent-yellow-500" />
        <label htmlFor="terms">
          I agree to the <span className="text-yellow-600 font-medium">Terms of Service</span> and
          <span className="text-yellow-600 font-medium"> Privacy Policy</span>
        </label>
      </div>
      <button
        type="submit"
        className="mt-2 bg-yellow-500 hover:bg-yellow-600 text-black font-semibold py-2 rounded-md transition-colors"
      >
        Create account
      </button>
    </form>
  );
};

export default SignupForm;
