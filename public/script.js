const form = document.getElementById('result-form');
const resultCard = document.getElementById('result-card');
const resName = document.getElementById('res-name');
const resRoll = document.getElementById('res-roll');
const resTotal = document.getElementById('res-total');
const resPercentage = document.getElementById('res-percentage');
const resGrade = document.getElementById('res-grade');

document.getElementById('year').textContent = new Date().getFullYear();

function encodeForm(data) {
  return Object.entries(data)
    .map(([k, v]) => `${encodeURIComponent(k)}=${encodeURIComponent(v)}`)
    .join('&');
}

function validateMarks(value) {
  const n = Number(value);
  return Number.isFinite(n) && n >= 0 && n <= 100;
}

function calcLocal({ name, roll, math, physics, chemistry, english, cs }) {
  const nums = [math, physics, chemistry, english, cs].map((v) => Math.min(100, Math.max(0, Number(v) || 0)));
  const total = nums.reduce((a, b) => a + b, 0);
  const percentage = (total * 100) / (nums.length * 100);
  let grade = 'F';
  if (percentage >= 90) grade = 'A+';
  else if (percentage >= 80) grade = 'A';
  else if (percentage >= 70) grade = 'B';
  else if (percentage >= 60) grade = 'C';
  else if (percentage >= 50) grade = 'D';
  return { name, rollNumber: roll, total, percentage, grade, subjectsCount: nums.length, maxMarksPerSubject: 100 };
}

async function requestCalculate(payload) {
  try {
    // Try POST first
    let response = await fetch('/api/calculate', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: encodeForm(payload),
    });

    // If server replies Method Not Allowed, retry with GET
    if (response.status === 405) {
      const qs = encodeForm(payload);
      response = await fetch(`/api/calculate?${qs}`, { method: 'GET' });
    }

    if (!response.ok) {
      const errorText = await response.text().catch(() => '');
      throw new Error(`Server error: ${response.status} ${errorText}`);
    }

    return await response.json();
  } catch (e) {
    // Fallback to local calculation if server fails
    console.warn('API failed, falling back to local calculation:', e);
    return calcLocal(payload);
  }
}

form.addEventListener('submit', async (e) => {
  e.preventDefault();

  const name = document.getElementById('name').value.trim();
  const roll = document.getElementById('roll').value.trim();
  const math = document.getElementById('math').value.trim();
  const physics = document.getElementById('physics').value.trim();
  const chemistry = document.getElementById('chemistry').value.trim();
  const english = document.getElementById('english').value.trim();
  const cs = document.getElementById('cs').value.trim();

  const fields = [math, physics, chemistry, english, cs];
  if (!fields.every(validateMarks)) {
    alert('Please enter valid marks between 0 and 100 for all subjects.');
    return;
  }

  try {
    const data = await requestCalculate({ name, roll, math, physics, chemistry, english, cs });

    resName.textContent = data.name || '—';
    resRoll.textContent = data.rollNumber || '—';
    resTotal.textContent = data.total?.toString() ?? '—';
    resPercentage.textContent = `${(data.percentage ?? 0).toFixed(2)}%`;
    resGrade.textContent = data.grade || '—';

    resultCard.classList.remove('hidden');
    resultCard.classList.remove('fade-in');
    void resultCard.offsetWidth; // restart animation
    resultCard.classList.add('fade-in');

    document.getElementById('results').scrollIntoView({ behavior: 'smooth', block: 'start' });
  } catch (err) {
    console.error('Unexpected error:', err);
    alert(`There was a problem calculating the result: ${err.message}`);
  }
});


