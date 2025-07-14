import { useEffect, useState } from "react";
import { User as UserIcon, Store, Image as ImageIcon, Lock } from "lucide-react";
import NavBar from "../components/NavBar";
import InputField from "../components/InputField";
import { apiFetch } from "../utils/api";

const initialShopForm = {
  shopName: "",
  shopAddress: "",
  shopPhone: "",
  shopDescription: "",
};

const initialUserForm = {
  name: "",
  phoneNumber: "",
  email: "",
  address: "",
  whatsappNumber: "",
  instagramHandle: "",
  websiteUrl: "",
};

const initialPasswordForm = {
  currentPassword: "",
  newPassword: "",
  confirmPassword: "",
};

const ProfilePage = () => {
  const [userForm, setUserForm] = useState(initialUserForm);
  const [passwordForm, setPasswordForm] = useState(initialPasswordForm);
  const [avatarPreview, setAvatarPreview] = useState(null);
  const [logoPreview, setLogoPreview] = useState(null);
  const [shopForm, setShopForm] = useState(initialShopForm);
  const [loading, setLoading] = useState(false);
  const [shopId, setShopId] = useState(null);
  const [userLoaded, setUserLoaded] = useState(false);

  // Load current user & their single shop
  const loadProfile = async () => {
    try {
      const list = await apiFetch("/api/shop/list");
      if (list.length) {
        const shop = list[0];
        setShopId(shop.id);
        setShopForm((prev) => ({
          ...prev,
          shopName: shop.shopName || "",
          shopAddress: shop.shopAddress || "",
          shopPhone: shop.shopPhone || "",
          shopDescription: shop.shopDescription || "",
        }));
      }
    } catch (err) {
      alert(err.message);
    }
  };

  const loadUser = async () => {
    try {
      const data = await apiFetch("/api/auth/me");
      setUserForm({
        name: data.name || "",
        phoneNumber: data.phoneNumber || "",
        email: data.email || "",
        address: data.address || "",
        whatsappNumber: data.whatsappNumber || "",
        instagramHandle: data.instagramHandle || "",
        websiteUrl: data.websiteUrl || "",
      });
      setUserLoaded(true);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    loadProfile();
    loadUser();
  }, []);

  const handleUserChange = (e) => {
    const { name, value } = e.target;
    setUserForm((prev) => ({ ...prev, [name]: value }));
  };

  const handlePasswordChange = (e) => {
    const { name, value } = e.target;
    setPasswordForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e) => {
    const { name, files } = e.target;
    if (!files?.length) return;
    const url = URL.createObjectURL(files[0]);
    if (name === "avatar") setAvatarPreview(url);
    if (name === "logo") setLogoPreview(url);
  };

  const handleShopChange = (e) => {
    const { name, value } = e.target;
    setShopForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      // Update user first
      await apiFetch("/api/user/update", {
        method: "PUT",
        body: JSON.stringify(userForm),
      });

      if (shopId) {
        await apiFetch(`/api/shop/${shopId}/update`, {
          method: "PUT",
          body: JSON.stringify(shopForm),
        });
      } else {
        await apiFetch("/api/shop/create", {
          method: "POST",
          body: JSON.stringify(shopForm),
        });
        await loadProfile();
      }
      alert("Saved");
    } catch (err) {
      alert(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex flex-col text-gray-900" style={{ backgroundImage: 'url("data:image/svg+xml;base64,PHN2ZyB3aWR0aD0nNjAnIGhlaWdodD0nNjAnIHhtbG5zPSdodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2Zyc+PHBhdGggZD0nTTAgNjAgNjAgMCA2MCA2MCAwIDYwJyBmaWxsPSdub25lJyBzdHJva2U9JyNmZmU4OGQnIHN0cm9rZS13aWR0aD0nMC41Jy8+PC9zdmc+")', backgroundSize: '60px 60px', backgroundColor: '#fefce8' }}> 
      <NavBar />
      {/*
      
        <svg className="w-full h-24 md:h-32" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 120" preserveAspectRatio="none">
          <path fill="#fef08a" d="M0 96L48 85.3C96 75 192 53 288 48C384 43 480 53 576 58.7C672 64 768 64 864 58.7C960 53 1056 43 1152 32C1248 21 1344 11 1392 5.3L1440 0V120H1392C1344 120 1248 120 1152 120C1056 120 960 120 864 120C768 120 672 120 576 120C480 120 384 120 288 120C192 120 96 120 48 120H0V96Z" />
        </svg>
      </div>
      
        <svg className="w-full h-24 md:h-32" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 120" preserveAspectRatio="none">
          <path fill="#fde047" d="M0 96L48 85.3C96 75 192 53 288 48C384 43 480 53 576 58.7C672 64 768 64 864 58.7C960 53 1056 43 1152 32C1248 21 1344 11 1392 5.3L1440 0V120H1392C1344 120 1248 120 1152 120C1056 120 960 120 864 120C768 120 672 120 576 120C480 120 384 120 288 120C192 120 96 120 48 120H0V96Z" />
        </svg>
      </div>
      */}
      <main className="flex-1 p-6 max-w-4xl mx-auto w-full">
        
        <form
          onSubmit={handleSubmit}
          className="flex flex-col gap-8"
        >
          {/* User details */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 bg-white border border-yellow-200 rounded-xl p-8 shadow-lg">
            <h2 className="md:col-span-2 flex items-center gap-2 font-semibold mb-4 text-yellow-600"><UserIcon size={20}/>User Details</h2>
            <InputField
              name="name"
              label="Name"
              placeholder="Your name"
              value={userForm.name}
              onChange={handleUserChange}
            />
            <InputField
              name="email"
              label="Email"
              type="email"
              placeholder="Email"
              value={userForm.email}
              onChange={handleUserChange}
            />
            <InputField
              name="phoneNumber"
              label="Phone Number"
              placeholder="Phone number"
              value={userForm.phoneNumber}
              onChange={handleUserChange}
            />
            <InputField
              name="address"
              label="Address"
              placeholder="Address"
              value={userForm.address}
              onChange={handleUserChange}
            />
            <InputField
              name="whatsappNumber"
              label="WhatsApp Number"
              placeholder="WhatsApp"
              value={userForm.whatsappNumber}
              onChange={handleUserChange}
            />
            <InputField
              name="instagramHandle"
              label="Instagram Handle"
              placeholder="@username"
              value={userForm.instagramHandle}
              onChange={handleUserChange}
            />
            <InputField
              name="websiteUrl"
              label="Website URL"
              placeholder="https://"
              value={userForm.websiteUrl}
              onChange={handleUserChange}
            />
          </div>

          {/* Shop details */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 bg-white border border-yellow-200 rounded-xl p-8 shadow-lg">
            <h2 className="md:col-span-3 flex items-center gap-2 font-semibold mb-4 text-yellow-600"><Store size={20}/>Shop Details</h2>
            <InputField
              name="shopName"
              label="Shop Name"
              placeholder="Shop name"
              value={shopForm.shopName}
              onChange={handleShopChange}
            />
          <InputField
              name="shopAddress"
              label="Shop Address"
              placeholder="Shop address"
              value={shopForm.shopAddress}
              onChange={handleShopChange}
            />
          <InputField
              name="shopPhone"
              label="Shop Phone"
              placeholder="Phone number"
              value={shopForm.shopPhone}
              onChange={handleShopChange}
            />
            <InputField
              name="shopDescription"
              label="Description"
              placeholder="Description"
              value={shopForm.shopDescription}
              onChange={handleShopChange}
            />
          </div>

          {/* Avatar & Logo */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 bg-white border border-yellow-200 rounded-xl p-8 shadow-lg">
            <h2 className="md:col-span-2 flex items-center gap-2 font-semibold mb-4 text-yellow-600"><ImageIcon size={20}/>Images</h2>
            <div className="flex flex-col gap-2 items-start">
              <label className="font-medium text-sm text-gray-700">Avatar</label>
              {avatarPreview ? (
              <img src={avatarPreview} alt="avatar" className="w-24 h-24 rounded-full object-cover border" />
            ) : (
              <div className="w-24 h-24 rounded-full bg-gray-100 flex items-center justify-center text-xs text-gray-500">No Avatar</div>
            )}
              <input type="file" name="avatar" accept="image/*" onChange={handleFileChange} />
            </div>
            <div className="flex flex-col gap-2 items-start">
              <label className="font-medium text-sm text-gray-700">Shop Logo</label>
              {logoPreview ? (
              <img src={logoPreview} alt="logo" className="w-24 h-24 object-cover border" />
            ) : (
              <div className="w-24 h-24 bg-gray-100 flex items-center justify-center text-xs text-gray-500">No Logo</div>
            )}
              <input type="file" name="logo" accept="image/*" onChange={handleFileChange} />
            </div>
          </div>

          {/* Change Password */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 bg-white border border-yellow-200 rounded-xl p-8 shadow-lg">
            <h2 className="md:col-span-3 flex items-center gap-2 font-semibold mb-4 text-yellow-600"><Lock size={20}/>Change Password</h2>
            <InputField
              name="currentPassword"
              label="Current Password"
              type="password"
              placeholder="Current Password"
              value={passwordForm.currentPassword}
              onChange={handlePasswordChange}
            />
            <InputField
              name="newPassword"
              label="New Password"
              type="password"
              placeholder="New Password"
              value={passwordForm.newPassword}
              onChange={handlePasswordChange}
            />
            <InputField
              name="confirmPassword"
              label="Confirm Password"
              type="password"
              placeholder="Confirm Password"
              value={passwordForm.confirmPassword}
              onChange={handlePasswordChange}
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="bg-yellow-500 hover:bg-yellow-600 text-black font-semibold py-2 rounded-md mt-2"
          >
            Save
          </button>
        </form>
      </main>
    </div>
  );
};

export default ProfilePage;
