import { Mail, Lock, User } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import InputField from "./InputField";

const SignupForm = () => {
  const navigate = useNavigate();
  const [name, setName] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      alert("Passwords do not match");
      return;
    }
    try {
      const res = await fetch("http://localhost:8080/api/auth/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ name, phoneNumber, email, password }),
      });
      if (!res.ok) {
        throw new Error("Failed to create account");
      }
      // On success we can auto-login by hitting login or just redirect
      navigate("/login");
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };
  return (
    <form onSubmit={handleSubmit} className="w-full max-w-md flex flex-col gap-6">
      <InputField
        name="name"
        label="Full name"
        type="text"
        icon={User}
        placeholder="Enter your full name"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />
      <InputField
        name="phoneNumber"
        label="Phone number"
        type="tel"
        icon={User}
        placeholder="Enter your phone number"
        value={phoneNumber}
        onChange={(e) => setPhoneNumber(e.target.value)}
      />
      <InputField
        name="email"
        label="Email address"
        type="email"
        icon={Mail}
        placeholder="Enter your email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <InputField
        name="password"
        label="Password"
        type="password"
        icon={Lock}
        placeholder="Create a password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <InputField
        name="confirmPassword"
        label="Confirm password"
        type="password"
        icon={Lock}
        placeholder="Confirm your password"
        value={confirmPassword}
        onChange={(e) => setConfirmPassword(e.target.value)}
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
