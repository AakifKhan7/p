import NavBar from "../components/NavBar";

const HomePage = () => {
  return (
    <div className="min-h-screen flex flex-col bg-white text-gray-900">
      <NavBar />
      <div className="flex-1 flex items-center justify-center">
        <h1 className="text-3xl font-bold">Welcome to Rentally</h1>
      </div>
    </div>
  );
};

export default HomePage;
