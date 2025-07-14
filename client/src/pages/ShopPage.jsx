import { useEffect, useState } from "react";
import NavBar from "../components/NavBar";
import { apiFetch } from "../utils/api";
import InputField from "../components/InputField";

const initialForm = {
  shopName: "",
  shopAddress: "",
  shopPhone: "",
};

const ShopPage = () => {
  const [shops, setShops] = useState([]);
  const [form, setForm] = useState(initialForm);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(false);

  const loadShops = async () => {
    try {
      const data = await apiFetch("/api/shop/list");
      setShops(data);
    } catch (err) {
      alert(err.message);
    }
  };

  useEffect(() => {
    loadShops();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      if (editingId) {
        await apiFetch(`/api/shop/${editingId}/update`, {
          method: "PUT",
          body: JSON.stringify(form),
        });
      } else {
        await apiFetch("/api/shop/create", {
          method: "POST",
          body: JSON.stringify(form),
        });
      }
      setForm(initialForm);
      setEditingId(null);
      await loadShops();
    } catch (err) {
      alert(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (shop) => {
    setForm({
      shopName: shop.shopName,
      shopAddress: shop.shopAddress,
      shopPhone: shop.shopPhone,
    });
    setEditingId(shop.id);
  };

  const handleDelete = async (id) => {
    if (!confirm("Delete this shop?")) return;
    try {
      await apiFetch(`/api/shop/${id}/delete`, { method: "DELETE" });
      await loadShops();
    } catch (err) {
      alert(err.message);
    }
  };

  return (
    <div className="min-h-screen flex flex-col bg-white text-gray-900">
      <NavBar />
      <main className="flex-1 p-6 max-w-5xl mx-auto w-full">
        <h1 className="text-2xl font-bold mb-6">Manage Shops</h1>

        {/* Form */}
        <form
          onSubmit={handleSubmit}
          className="grid grid-cols-1 md:grid-cols-3 gap-4 bg-gray-50 p-4 rounded-md border border-gray-200 mb-8"
        >
          <InputField
            name="shopName"
            label="Shop Name"
            placeholder="Shop name"
            value={form.shopName}
            onChange={handleChange}
          />
          <InputField
            name="shopAddress"
            label="Shop Address"
            placeholder="Shop address"
            value={form.shopAddress}
            onChange={handleChange}
          />
          <InputField
            name="shopPhone"
            label="Shop Phone"
            placeholder="Phone number"
            value={form.shopPhone}
            onChange={handleChange}
          />
          <button
            type="submit"
            disabled={loading}
            className="md:col-span-3 bg-yellow-500 hover:bg-yellow-600 text-black font-semibold py-2 rounded-md"
          >
            {editingId ? "Update Shop" : "Add Shop"}
          </button>
        </form>

        {/* List */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {shops.map((shop) => (
            <div
              key={shop.id}
              className="border border-gray-200 rounded-md p-4 flex flex-col gap-2"
            >
              <h2 className="font-semibold text-lg">{shop.shopName}</h2>
              <p className="text-sm text-gray-600">{shop.shopAddress}</p>
              <p className="text-sm text-gray-600">{shop.shopPhone}</p>
              <div className="mt-auto flex gap-2 text-xs">
                <button
                  onClick={() => handleEdit(shop)}
                  className="bg-yellow-500 hover:bg-yellow-600 text-black font-semibold px-3 py-1 rounded-md"
                >
                  Edit
                </button>
                <button
                  onClick={() => handleDelete(shop.id)}
                  className="bg-red-500 hover:bg-red-600 text-white font-semibold px-3 py-1 rounded-md"
                >
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      </main>
    </div>
  );
};

export default ShopPage;
