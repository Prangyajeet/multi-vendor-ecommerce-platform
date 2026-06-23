function StatCard({ title, value }) {
  return (
    <div className="bg-white rounded-xl shadow p-4">
      <h3>{title}</h3>

      <p className="text-2xl font-bold">
        {value}
      </p>
    </div>
  );
}

export default StatCard;