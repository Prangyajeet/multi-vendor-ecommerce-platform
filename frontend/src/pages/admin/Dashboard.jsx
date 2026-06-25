import AdminLayout from "../../layouts/AdminLayout";
import PageTitle from "../../components/common/PageTitle";
import StatCard from "../../components/cards/StatCard";

function Dashboard() {
  return (
    <AdminLayout>

      <PageTitle title="Admin Dashboard" />

      <div className="grid grid-cols-4 gap-4">

        <StatCard
          title="Users"
          value="150"
        />

        <StatCard
          title="Orders"
          value="85"
        />

      </div>

    </AdminLayout>
  );
}

export default Dashboard;