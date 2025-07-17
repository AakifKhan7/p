import React from 'react';
import { Link } from 'react-router-dom';

const HeroSection = () => {
  return (
    <section className="bg-gradient-to-r from-blue-600 to-indigo-600 text-white py-20">
      <div className="max-w-6xl mx-auto px-6 text-center">
        <h1 className="text-4xl md:text-6xl font-extrabold mb-6">Dukandar Digital Hisab App</h1>
        <p className="text-lg md:text-2xl mb-8">Replace your notebook with a smart voice-entry ledger and manage sales effortlessly.</p>
        <div className="flex flex-col sm:flex-row justify-center gap-4">
          <Link to="/login" className="bg-white text-blue-600 px-8 py-3 rounded-lg font-semibold shadow hover:bg-gray-100 transition">Start Free Trial</Link>
          <a href="#features" className="bg-transparent border border-white px-8 py-3 rounded-lg font-semibold hover:bg-white hover:text-blue-600 transition">Learn More</a>
        </div>
      </div>
    </section>
  );
};

export default HeroSection;
