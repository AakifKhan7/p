import { useEffect, useState, useRef } from "react";
import { PlusCircle, Trash2 } from "lucide-react";
import InputField from "../components/InputField";
import NavBar from "../components/NavBar";
import { apiFetch } from "../utils/api";

// Guard to avoid duplicate initial fetch in React StrictMode dev re-mount
let initialInventoryLoaded = false;

const emptyForm = {
    productName: "",
    quantity: "",
    price: "",
    shopId: "",
};

export default function InventoryPage() {
    const [inventory, setInventory] = useState([]);
    const [shops, setShops] = useState([]);
    const [form, setForm] = useState(emptyForm);
    const [loading, setLoading] = useState(false);

    const loadShops = async () => {
        try {
            const res = await apiFetch("/api/shop/list");
            setShops(res);
            if (res.length === 1) {
                setForm((f) => ({ ...f, shopId: res[0].id }));
            }
        } catch (e) {
            console.error(e);
            alert("Failed to load shops");
        }
    };

    const loadInventory = async () => {
        try {
            const res = await apiFetch("/api/inventory/shop/" + form.shopId);
            setInventory(res);
        } catch (e) {
            console.error(e);
            alert("Failed to load inventory");
        }
    };

    const didInit = useRef(false);
    useEffect(() => {
        if (didInit.current) return;
        didInit.current = true;
        loadShops();
        if (!initialInventoryLoaded && shops.length === 1) {
            loadInventory();
            initialInventoryLoaded = true;
        }
    }, [shops]);

    // periodic refresh every 60 s without spamming
    useEffect(() => {
        const id = setInterval(loadInventory, 60000);
        return () => clearInterval(id);
    }, [form.shopId]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm((f) => ({ ...f, [name]: value }));
    };

    const resetForm = () => setForm((f) => ({ ...emptyForm, shopId: f.shopId }));

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!form.shopId) {
            alert("Please select a shop");
            return;
        }
        try {
            setLoading(true);
            await apiFetch("/api/inventory", {
                method: "POST",
                body: JSON.stringify({
                    ...form,
                    quantity: parseInt(form.quantity, 10),
                    price: parseFloat(form.price),
                }),
            });
            resetForm();
            await loadInventory();
        } catch (err) {
            alert(err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Delete this inventory item?")) return;
        try {
            await apiFetch(`/api/inventory/${id}/delete`, {
                method: "DELETE",
            });
            await loadInventory();
        } catch (e) {
            alert(e.message);
        }
    };

    return (
        <div
            className="min-h-screen flex flex-col text-gray-900"
            style={{
                backgroundImage:
                    'url("data:image/svg+xml;base64,PHN2ZyB3aWR0aD0nNjAnIGhlaWdodD0nNjAnIHhtbG5zPSdodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2Zyc+PHBhdGggZD0nTTAgNjAgNjAgMCA2MCA2MCAwIDYwJyBmaWxsPSdub25lJyBzdHJva2U9JyNmZmU4OGQnIHN0cm9rZS13aWR0aD0nMC41Jy8+PC9zdmc=")',
                backgroundSize: "60px 60px",
                backgroundColor: "#fefce8",
            }}
        >
            <NavBar />

            <main className="flex-1 p-6 max-w-4xl mx-auto w-full flex flex-col gap-8">
                {/* Shop dropdown if multiple */}
                {shops.length > 1 && (
                    <div className="flex flex-col gap-1 w-full">
                        <label className="font-medium text-sm text-gray-700">Shop</label>
                        <select
                            name="shopId"
                            value={form.shopId}
                            onChange={handleChange}
                            className="w-full border border-gray-300 rounded-md py-2 px-3 text-sm focus:outline-none focus:border-yellow-500"
                        >
                            <option value="">Select shop</option>
                            {shops.map((s) => (
                                <option key={s.id} value={s.id}>
                                    {s.name}
                                </option>
                            ))}
                        </select>
                    </div>
                )}

                {/* Add Inventory Form */}
                <form
                    onSubmit={handleSubmit}
                    className="bg-white border border-yellow-200 rounded-xl p-8 shadow-lg flex flex-col gap-6"
                >
                    <h2 className="flex items-center gap-2 font-semibold text-yellow-600 text-lg">
                        <PlusCircle size={20} /> Add to Inventory
                    </h2>

                    <InputField
                        label="Product Name"
                        name="productName"
                        value={form.productName}
                        onChange={handleChange}
                        placeholder="Enter product name"
                    />
                    <InputField
                        label="Quantity"
                        name="quantity"
                        type="number"
                        value={form.quantity}
                        onChange={handleChange}
                        min="1"
                    />
                    <InputField
                        label="Price"
                        name="price"
                        value={form.price}
                        onChange={handleChange}
                        placeholder="0.00"
                        type="number"
                        min="0"
                    />

                    <button
                        type="submit"
                        disabled={loading}
                        className="self-start inline-flex items-center gap-2 bg-yellow-500 hover:bg-yellow-600 text-white px-4 py-2 rounded-md text-sm"
                    >
                        <PlusCircle size={16} /> {loading ? "Saving..." : "Add to Inventory"}
                    </button>
                </form>

                {/* Inventory List */}
                <div className="bg-white border border-yellow-200 rounded-xl p-8 shadow-lg flex flex-col gap-6">
                    <h2 className="flex items-center gap-2 font-semibold text-yellow-600 text-lg">
                        <PlusCircle size={20} /> Inventory
                    </h2>

                    {inventory.length === 0 ? (
                        <p className="text-sm text-gray-500">No inventory items found.</p>
                    ) : (
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            {inventory.map((item) => (
                                <div
                                    key={item.id}
                                    className="border border-gray-200 rounded-md p-4 flex flex-col gap-2 relative"
                                >
                                    <h3 className="font-medium text-gray-800">{item.productName}</h3>
                                    <p className="text-sm text-gray-600">Quantity: {item.quantity}</p>
                                    <p className="text-sm text-gray-600">Price: ₹{item.price}</p>

                                    <button
                                        type="button"
                                        onClick={() => handleDelete(item.id)}
                                        className="absolute top-2 right-2 text-red-500 hover:text-red-600"
                                    >
                                        <Trash2 size={18} />
                                    </button>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </main>
        </div>
    );
}
