import React, { useState } from 'react';

const activityMultipliers: Record<string, number> = {
  sedentary: 1.2,
  light: 1.375,
  moderate: 1.55,
  active: 1.725,
  very_active: 1.9,
};

const indianDiets = {
  maintain: [
    'Breakfast: Vegetable upma + curd',
    'Snack: Fruit bowl',
    'Lunch: Dal, roti, sabzi, salad',
    'Snack: Buttermilk + roasted chana',
    'Dinner: Paneer bhurji + brown rice + salad',
  ],
  gain: [
    'Breakfast: Paneer paratha + banana + milk',
    'Snack: Dry fruits + mango shake',
    'Lunch: Rice, dal, chicken curry(non veg)/rajma, mixed veg, salad',
    'Snack: Peanut butter sandwich + lassi',
    'Dinner: Roti, paneer sabzi, dal, ghee, salad',
    'Bedtime: Milk with honey',
  ],
  lose: [
    'Breakfast: Oats porridge + boiled eggs',
    'Snack: Sprouts salad',
    'Lunch: Grilled chicken/fish(Non veg) + dal + sabzi + salad',
    'Snack: Green tea + roasted makhana',
    'Dinner: Moong dal chilla + sautéed veggies',
  ],
};

const proteinSources = [
  'whey protien(MuscleBlaze,ON,Avaatar,etc)',
  'Paneer',
  'Soya chunks (50g daily)',
  'Peanut butter',
  'Lentils (rajma, chickpeas/cholay, moong)',
  'Tofu',
  'Eggs',
  'Greek yogurt',
  'Chicken breast(non veg)',
  'Fish(non veg)',
  'Milk',
];

const BMRCalculator: React.FC = () => {
  const [age, setAge] = useState('');
  const [weight, setWeight] = useState('');
  const [height, setHeight] = useState('');
  const [gender, setGender] = useState('male');
  const [activity, setActivity] = useState('sedentary');
  const [goal, setGoal] = useState<'maintain' | 'gain' | 'lose'>('maintain');
  const [bmr, setBmr] = useState<number | null>(null);
  const [calories, setCalories] = useState<number | null>(null);
  const [diet, setDiet] = useState<string[]>([]);
  const [resultType, setResultType] = useState<'maintain' | 'gain' | 'lose'>('maintain');
  const [protein, setProtein] = useState<number | null>(null);

  const calculateBMR = (e: React.FormEvent) => {
    e.preventDefault();
    const w = parseFloat(weight);
    const h = parseFloat(height);
    const a = parseInt(age);
    if (!w || !h || !a) return;
    let bmrValue = 0;
    // Mifflin-St Jeor equation
    if (gender === 'male') {
      bmrValue = 10 * w + 6.25 * h - 5 * a + 5;
    } else {
      bmrValue = 10 * w + 6.25 * h - 5 * a - 161;
    }
    setBmr(Math.round(bmrValue));
    const maintenance = bmrValue * activityMultipliers[activity];
    let displayCalories = maintenance;
    let type: 'maintain' | 'gain' | 'lose' = goal;
    if (goal === 'gain') displayCalories = maintenance + 400;
    if (goal === 'lose') displayCalories = maintenance - 400;
    setCalories(Math.round(displayCalories));
    setDiet(indianDiets[goal]);
    setResultType(type);
    // Protein intake: 1.6g/kg (maintain), 2g/kg (gain), 2.2g/kg (lose)
    let proteinGrams = 0;
    if (goal === 'maintain') proteinGrams = w * 1.6;
    if (goal === 'gain') proteinGrams = w * 2.0;
    if (goal === 'lose') proteinGrams = w * 2.2;
    setProtein(Math.round(proteinGrams));
  };

  return (
    <div className="bg-white rounded shadow p-6 w-full max-w-lg mx-auto">
      <h3 className="text-xl font-bold mb-4">BMR Calculator</h3>
      <form className="flex flex-col gap-4" onSubmit={calculateBMR}>
        <input type="number" placeholder="Age" value={age} onChange={e => setAge(e.target.value)} className="border p-2 rounded" required />
        <input type="number" placeholder="Weight (kg)" value={weight} onChange={e => setWeight(e.target.value)} className="border p-2 rounded" required />
        <input type="number" placeholder="Height (cm)" value={height} onChange={e => setHeight(e.target.value)} className="border p-2 rounded" required />
        <select value={gender} onChange={e => setGender(e.target.value)} className="border p-2 rounded">
          <option value="male">Male</option>
          <option value="female">Female</option>
        </select>
        <select value={activity} onChange={e => setActivity(e.target.value)} className="border p-2 rounded">
          <option value="sedentary">Sedentary (little or no exercise)</option>
          <option value="light">Lightly active (1-3 days/week)</option>
          <option value="moderate">Moderately active (3-5 days/week)</option>
          <option value="active">Active (6-7 days/week)</option>
          <option value="very_active">Very active (hard exercise & physical job)</option>
        </select>
        <select value={goal} onChange={e => setGoal(e.target.value as any)} className="border p-2 rounded">
          <option value="maintain">Maintain</option>
          <option value="gain">Gain Weight</option>
          <option value="lose">Lose Weight</option>
        </select>
        <button type="submit" className="bg-blue-600 text-white py-2 rounded">Calculate BMR</button>
      </form>
      {bmr !== null && calories !== null && protein !== null && (
        <div className="mt-8 p-6 rounded-2xl shadow-lg bg-gradient-to-br from-blue-50 to-white border border-blue-100">
          <div className="text-lg font-bold mb-2 text-blue-700">BMR: <span className="text-2xl">{bmr} kcal/day</span></div>
          <div className="text-md font-semibold mb-4 text-green-700">
            {resultType === 'maintain' && (
              <>Maintenance Calories: <span className="text-xl text-blue-800">{calories} kcal</span></>
            )}
            {resultType === 'gain' && (
              <>Calorie Surplus: <span className="text-xl text-orange-600">{calories} kcal</span></>
            )}
            {resultType === 'lose' && (
              <>Calorie Deficit: <span className="text-xl text-pink-600">{calories} kcal</span></>
            )}
          </div>
          <div className="mb-2 text-base font-semibold text-gray-700 flex items-center gap-2">
            {resultType === 'maintain' && <span>🍽️ Indian Maintenance Diet</span>}
            {resultType === 'gain' && <span>🍛 Indian Bulking Diet</span>}
            {resultType === 'lose' && <span>🥗 Indian Cutting Diet</span>}
          </div>
          <ul className="list-disc list-inside text-gray-700 space-y-1 mb-4">
            {diet.map((item, idx) => <li key={idx}>{item}</li>)}
          </ul>
          <div className="mt-4 p-4 rounded-xl bg-white border border-blue-200 shadow flex flex-col items-start">
            <div className="font-semibold text-blue-800 mb-2">Daily Protein Intake: <span className="text-lg text-green-700">{protein}g</span></div>
            <div className="font-medium text-gray-700 mb-1">Indian Protein Sources:</div>
            <ul className="list-disc list-inside text-gray-700 space-y-1">
              {proteinSources.map((item, idx) => <li key={idx}>{item}</li>)}
            </ul>
          </div>
        </div>
      )}
    </div>
  );
};

export default BMRCalculator; 