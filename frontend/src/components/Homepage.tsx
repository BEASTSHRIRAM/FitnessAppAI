import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Calculator, TrendingUp, Dumbbell, Trophy, Calendar, UserCheck } from 'lucide-react';

const featureCards = [
  {
    label: 'BMR Calculator(Calories,Diet Plan)',
    icon: <Calculator className="h-8 w-8 text-blue-600" />,
    route: '/bmr',
    external: false,
  },
  {
    label: 'Progress Entries',
    icon: <TrendingUp className="h-8 w-8 text-green-600" />,
    route: '/progress',
    external: false,
  },
  {
    label: 'My Workouts',
    icon: <Dumbbell className="h-8 w-8 text-purple-600" />,
    route: '/workouts',
    external: false,
  },
  {
    label: 'Personal Records',
    icon: <Trophy className="h-8 w-8 text-yellow-600" />,
    route: '/pr',
    external: false,
  },
  {
    label: 'Workout Recommendations',
    icon: <Calendar className="h-8 w-8 text-pink-600" />,
    route: '/recommendations',
    external: false,
  },
  {
    label: 'Coaching',
    icon: <UserCheck className="h-8 w-8 text-orange-600" />,
    route: 'https://forms.gle/jmkhG3uHGamKNiaF7',
    external: true,
  },
];

const Homepage: React.FC = () => {
  const navigate = useNavigate();

  const handleCardClick = (card: typeof featureCards[0]) => {
    if (card.external) {
      window.open(card.route, '_blank');
    } else {
      navigate(card.route);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-blue-100 py-8 px-2 flex flex-col items-center gap-8">
      <h1 className="text-4xl font-bold mb-8 text-center text-blue-800 drop-shadow">🏠 Fitness Dashboard</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-8 w-full max-w-5xl">
        {featureCards.map(card => (
          <button
            key={card.label}
            onClick={() => handleCardClick(card)}
            className="flex flex-col items-center justify-center bg-white rounded-3xl shadow-xl p-10 transition-transform hover:scale-105 hover:shadow-2xl border-2 border-transparent hover:border-blue-300 focus:outline-none min-h-[180px]"
          >
            {card.icon}
            <span className="mt-4 text-xl font-semibold text-gray-800">{card.label}</span>
          </button>
        ))}
      </div>
    </div>
  );
};

export default Homepage; 