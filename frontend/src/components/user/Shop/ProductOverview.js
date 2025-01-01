import React, { useState, useEffect } from 'react';
import '../../../styles/productOverview.css';
import Carousel from "../Home/Carousel";
import ProductService from '../../../api/services/ProductService'; // Assuming this is the service file

const carouselImages = [
    {
        src: "https://objectstorage.ap-mumbai-1.oraclecloud.com/n/softlogicbicloud/b/cdn/o/products/400-600/193664152--1--1672131753.jpeg",
    },
    {
        src: "https://objectstorage.ap-mumbai-1.oraclecloud.com/n/softlogicbicloud/b/cdn/o/products/400-600/193664152--2--1672131754.jpeg",
    },
    {
        src: "https://objectstorage.ap-mumbai-1.oraclecloud.com/n/softlogicbicloud/b/cdn/o/products/400-600/193664152--3--1672131757.jpeg",
    },
];

const ProductOverview = () => {
    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [selectedCategoryId, setSelectedCategoryId] = useState(null);
    const [likedProducts, setLikedProducts] = useState([]);
    const [selectedProduct, setSelectedProduct] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [quantity, setQuantity] = useState(1);
    const [cart, setCart] = useState(() => {
        const savedCart = localStorage.getItem('cart');
        return savedCart ? JSON.parse(savedCart) : [];
    });

    useEffect(() => {
        const fetchData = async () => {
            try {
                const savedCategories = JSON.parse(localStorage.getItem('categoryList')) || [];
                setCategories(savedCategories);
                const response = await ProductService.getItems();
                setProducts(response.data || []);
            } catch (error) {
                console.error('Failed to load products or categories:', error.message);
            }
        };
        fetchData();
    }, []);

    useEffect(() => {
        localStorage.setItem('cart', JSON.stringify(cart));
    }, [cart]);

    const increaseQuantity = () => setQuantity(quantity + 1);
    const decreaseQuantity = () => {
        if (quantity > 1) setQuantity(quantity - 1);
    };

    const addToCart = (product) => {
        const existingProduct = cart.find((item) => item.id === product.id);

        if (existingProduct) {
            // Update quantity for an existing product
            const updatedCart = cart.map((item) =>
                item.id === product.id
                    ? { ...item, quantity: item.quantity + quantity }
                    : item
            );
            setCart(updatedCart);
            console.log("Updated product quantity:", updatedCart);
        } else {
            // Add new product to the cart
            const newCart = [...cart, { ...product, quantity }];
            setCart(newCart);
            console.log("Added new product:", newCart);
        }

        setQuantity(1); // Reset quantity
        console.log("Current cart state:", cart);
    };


    const removeFromCart = (productId) => {
        setCart(cart.filter((item) => item.id !== productId));
    };

    const filteredProducts = selectedCategoryId
        ? products.filter(product => product.categoryId === selectedCategoryId)
        : products;

    const toggleLike = (productId) => {
        if (likedProducts.includes(productId)) {
            setLikedProducts(likedProducts.filter(id => id !== productId));
        } else {
            setLikedProducts([...likedProducts, productId]);
        }
    };

    const openQuickView = (product) => {
        setSelectedProduct(product);
        setQuantity(1); // Reset quantity when opening Quick View
        setIsModalOpen(true);
    };

    const closeQuickView = () => {
        console.log('Closing modal');
        setSelectedProduct(null);
        setIsModalOpen(false);
    };

    return (
        <div className="product-overview">
            <header className="product-header">
                <h1>PRODUCT OVERVIEW</h1>
                <div className="product-categories">
                    <span
                        className={selectedCategoryId === null ? 'active' : ''}
                        onClick={() => setSelectedCategoryId(null)}
                    >
                        All Products
                    </span>
                    {categories.map((category) => (
                        <span
                            key={category.id}
                            className={selectedCategoryId === category.id ? 'active' : ''}
                            onClick={() => setSelectedCategoryId(category.id)}
                        >
                            {category.name}
                        </span>
                    ))}
                </div>
            </header>

            <div className="product-grid">
                {filteredProducts.length > 0 ? (
                    filteredProducts.map((product) => (
                        <div key={product.id} className="product-card">
                            <div className="product-image-wrapper">
                                {product.new && <span className="new-badge">New</span>}
                                <img src={product.imageURL} alt={product.name} className="product-image" />
                                <button className="quick-view-btn" onClick={() => openQuickView(product)}>
                                    Quick View
                                </button>
                            </div>
                            <div className="product-info">
                                <h3>{product.name}</h3>
                                <p>Price: ${product.price}</p>
                                <div className="like-icon" onClick={() => toggleLike(product.id)}>
                                    <i className={likedProducts.includes(product.id) ? 'fas fa-heart liked' : 'far fa-heart'}></i>
                                </div>
                            </div>
                        </div>
                    ))
                ) : (
                    <p>No products found in this category.</p>
                )}
            </div>

            {/* Quick View Modal */}
            {isModalOpen && selectedProduct && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white w-1/2 sm:w-[60%] h-[80vh] overflow-y-auto rounded-lg shadow-lg relative p-4 flex flex-col justify-between">
                        {/* Close Button */}
                        <button
                            onClick={closeQuickView}
                            className="absolute top-4 right-4 text-gray-600 hover:text-black md:top-6 md:right-6"
                            style={{
                                fontSize: "1.5rem",
                                padding: "0.5rem",
                                lineHeight: "1",
                            }}
                        >
                            &times;
                        </button>

                        <div className="flex flex-col h-full">
                            {/* Image Section */}
                            <div className="w-full flex justify-center items-center p-4">
                                <div className="w-1/3 h-auto">
                                    <Carousel images={carouselImages} isModal={true} /> {/* Image Carousel */}
                                </div>
                            </div>

                            {/* Details Section */}
                            <div className="w-full flex flex-col p-4 space-y-4">
                                <h2 className="text-xl md:text-2xl font-semibold">{selectedProduct.title}</h2>
                                <p className="text-lg md:text-xl text-gray-700">{selectedProduct.price}</p>
                                <p className="text-sm md:text-base text-gray-500">Some description about the product.</p>

                                {/* Size and Color Selection */}
                                <div className="space-y-4">
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700">Size</label>
                                        <select className="w-full border-gray-300 rounded-md">
                                            <option>Choose an option</option>
                                        </select>
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700">Color</label>
                                        <select className="w-full border-gray-300 rounded-md">
                                            <option>Choose an option</option>
                                        </select>
                                    </div>
                                </div>

                                {/* Quantity Selector */}
                                <div className="flex items-center space-x-4">
                                    <button onClick={decreaseQuantity} className="px-4 py-2 border rounded">-</button>
                                    <input
                                        type="number"
                                        value={quantity}
                                        readOnly
                                        className="w-16 text-center border rounded"
                                    />
                                    <button onClick={increaseQuantity} className="px-4 py-2 border rounded">+</button>
                                </div>

                                {/* Add to Cart Button */}
                                <button className="bg-purple-600 text-white px-6 py-3 rounded hover:bg-black transition-all duration-300">
                                    ADD TO CART
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}

        </div>
    );
};

export default ProductOverview;
