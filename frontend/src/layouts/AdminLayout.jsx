import Sidebar from "../components/sidebar/Sidebar";

function AdminLayout({ children }) {

  const menuItems = [
    "Dashboard",
    "Users",
    "Products",
    "Orders",
    "Reports",
  ];

  return (
    <div className="flex">

      <Sidebar menuItems={menuItems} />

      <main className="flex-1 p-6">
        {children}
      </main>

    </div>
  );
}

export default AdminLayout;