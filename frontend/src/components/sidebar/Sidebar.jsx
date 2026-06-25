function Sidebar({ menuItems }) {
  return (
    <aside className="w-64 bg-gray-800 text-white min-h-screen">

      <ul className="p-4 space-y-4">

        {menuItems.map((item) => (
          <li key={item}>
            {item}
          </li>
        ))}

      </ul>

    </aside>
  );
}

export default Sidebar;