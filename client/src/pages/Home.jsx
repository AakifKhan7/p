import React from 'react';
import HeroSection from '../components/HeroSection';
import FeaturesSection from '../components/FeaturesSection';
import CTASection from '../components/CTASection';
import Footer from '../components/Footer';

const Home = () => {
  return (
    <main className="font-sans">
      <HeroSection />
      <FeaturesSection />
      <CTASection />
      <Footer />
    </main>
  );
};

export default Home;
