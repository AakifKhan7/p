import React from 'react';
import { Link } from 'react-router-dom';

const CTASection = () => {
  return (
    <section className="py-20 bg-blue-600 text-white">
      <div className="max-w-4xl mx-auto px-6 text-center">
        <h2 className="text-3xl md:text-4xl font-bold mb-4">Ready to ditch the notebook?</h2>
        <p className="text-lg md:text-xl mb-8">Join hundreds of shopkeepers who have gone digital with Dukandar Digital Hisab.</p>
        <Link
          to="/login"
          className="inline-block bg-white text-blue-600 px-10 py-3 rounded-lg font-semibold shadow hover:bg-gray-100 transition"
        >
          Start Now
        </Link>
      </div>
    </section>
  );
};

export default CTASection;
