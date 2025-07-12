import Logo from "../components/Logo";
import SignupForm from "../components/SignupForm";
import FeaturesCard from "../components/FeaturesCard";

const SignupPage = () => {
  return (
    <div className="min-h-screen flex flex-col bg-white text-gray-900">
      {/* Header */}
      <header className="flex items-center justify-start p-6">
        <Logo />
      </header>

      {/* Main content */}
      <main className="flex-1 flex flex-col items-center px-4 md:px-0 md:flex-row md:items-stretch md:justify-center gap-16 w-full max-w-6xl mx-auto">
        {/* left - form */}
        <section className="flex flex-col gap-6 py-8 md:py-12 w-full md:w-1/2">
          <div className="flex flex-col gap-2">
            <h1 className="text-4xl font-extrabold">Create account</h1>
            <p className="text-gray-600 text-sm">Start managing your rental business today</p>
          </div>
          <SignupForm />
        </section>

        {/* right - features */}
        <aside className="hidden md:flex md:w-1/2 justify-center items-center py-8 md:py-12">
          <FeaturesCard />
        </aside>
      </main>
    </div>
  );
};

export default SignupPage;
