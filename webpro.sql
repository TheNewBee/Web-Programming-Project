-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- 主機: localhost
-- 產生時間： 2018-04-29 15:13:31
-- 伺服器版本: 10.2.8-MariaDB
-- PHP 版本： 7.1.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `webpro`
--

-- --------------------------------------------------------

--
-- 資料表結構 `buyer_order`
--

DROP TABLE IF EXISTS `buyer_order`;
CREATE TABLE `buyer_order` (
  `buyer_order_id` int(12) NOT NULL,
  `member_id` int(12) NOT NULL DEFAULT 0,
  `shop_id` int(12) NOT NULL DEFAULT 0,
  `buyer_order_promotion_id` int(12) NOT NULL DEFAULT 0,
  `total_price` double NOT NULL DEFAULT 0,
  `shipping_name` varchar(32) NOT NULL DEFAULT '''''',
  `shipping_contact_number` int(8) NOT NULL DEFAULT 0,
  `shipping_address` text NOT NULL,
  `status` tinyint(2) NOT NULL DEFAULT 1,
  `datetime` datetime NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `buyer_order`
--

INSERT INTO `buyer_order` (`buyer_order_id`, `member_id`, `shop_id`, `buyer_order_promotion_id`, `total_price`, `shipping_name`, `shipping_contact_number`, `shipping_address`, `status`, `datetime`) VALUES
(1, 1, 1, 0, 9999, 'aasd', 225552, 'asdasd', 1, '2018-04-26 14:47:00'),
(2, 1, 1, 0, 9999, 'aasd', 225552, 'asdasd', 1, '2018-04-26 14:47:31'),
(3, 3, 3, 4, 0, 'hkeasyship', 23345567, 'Hong Kong', 0, '2018-04-26 14:47:32'),
(4, 5, 3, 3, 0, 'hkshipzone', 24130558, 'Koera', 0, '2018-04-26 14:47:33'),
(5, 6, 4, 2, 0, 'hkshipzone', 24160558, 'Japan', 0, '2018-04-26 14:47:34'),
(6, 7, 2, 3, 0, 'shippingking', 26790013, 'UK', 0, '2018-04-26 14:47:35'),
(7, 8, 1, 1, 0, 'shippingking', 26790013, 'USA', 0, '2018-04-26 14:47:36'),
(8, 10, 1, 4, 0, 'hkeasyship', 23345567, 'New Zealand', 0, '2018-04-26 14:47:37');

-- --------------------------------------------------------

--
-- 資料表結構 `buyer_order_product`
--

DROP TABLE IF EXISTS `buyer_order_product`;
CREATE TABLE `buyer_order_product` (
  `buyer_order_id` int(12) NOT NULL DEFAULT 0,
  `product_id` int(12) NOT NULL DEFAULT 0,
  `product_type_id` int(12) NOT NULL DEFAULT 0,
  `quantity` int(5) NOT NULL DEFAULT 0,
  `product_price` double NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `buyer_order_product`
--

INSERT INTO `buyer_order_product` (`buyer_order_id`, `product_id`, `product_type_id`, `quantity`, `product_price`) VALUES
(1, 1, 2, 3, 9999),
(2, 1, 2, 2, 9999),
(1, 1, 0, 1, 9690.24),
(2, 9, 0, 1, 769.1),
(3, 2, 0, 10, 268),
(4, 19, 0, 1, 460),
(5, 14, 0, 2, 95.84),
(6, 5, 0, 2, 3930);

-- --------------------------------------------------------

--
-- 資料表結構 `buyer_order_promotion`
--

DROP TABLE IF EXISTS `buyer_order_promotion`;
CREATE TABLE `buyer_order_promotion` (
  `buyer_order_promotion_id` int(12) NOT NULL,
  `buyer_order_promotion_name` varchar(32) NOT NULL DEFAULT '',
  `buyer_order_promotion_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0=No promotion, 1=by persentage, 2=minus a price',
  `buyer_order_promotion_amount` double NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `buyer_order_promotion`
--

INSERT INTO `buyer_order_promotion` (`buyer_order_promotion_id`, `buyer_order_promotion_name`, `buyer_order_promotion_type`, `buyer_order_promotion_amount`) VALUES
(1, 'VIP', 1, 0.9),
(2, 'Super VIP', 1, 0.8),
(3, 'New shop coupon', 2, 20),
(4, 'Buy $300 Free $50', 2, 50);

-- --------------------------------------------------------

--
-- 資料表結構 `cart`
--

DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
  `member_id` int(12) NOT NULL DEFAULT 0,
  `product_id` int(12) NOT NULL DEFAULT 0,
  `product_type_id` int(12) NOT NULL DEFAULT 0,
  `quantity` int(5) NOT NULL DEFAULT 0,
  `added_on` timestamp NOT NULL DEFAULT current_timestamp(),
  `last_modified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `cart`
--

INSERT INTO `cart` (`member_id`, `product_id`, `product_type_id`, `quantity`, `added_on`, `last_modified`) VALUES
(1, 1, 1, 3, '2018-04-26 00:28:30', '2018-04-26 00:28:30'),
(2, 1, 1, 5, '2018-04-26 01:36:12', '2018-04-26 01:36:13'),
(3, 1, 1, 1, '2018-04-25 03:00:52', '2018-04-25 03:15:52'),
(5, 9, 1, 1, '2018-04-24 03:50:19', '2018-04-24 04:10:19'),
(6, 2, 1, 10, '2018-04-25 18:00:25', '2018-04-25 18:29:25'),
(7, 19, 1, 1, '2018-04-23 21:30:53', '2018-04-25 03:00:56'),
(8, 14, 1, 2, '2018-04-18 21:15:20', '2018-04-18 21:35:20'),
(10, 5, 1, 2, '2018-04-23 05:00:26', '2018-04-23 05:20:00');

-- --------------------------------------------------------

--
-- 資料表結構 `member`
--

DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `member_id` int(12) NOT NULL,
  `email` varchar(255) NOT NULL DEFAULT '',
  `password` varchar(255) NOT NULL DEFAULT '',
  `nickname` varchar(255) NOT NULL DEFAULT '',
  `member_level` tinyint(2) NOT NULL DEFAULT 1,
  `amount_bought` int(16) NOT NULL DEFAULT 0,
  `has_shop` tinyint(1) NOT NULL DEFAULT 0,
  `disabled` tinyint(1) NOT NULL DEFAULT 0,
  `regdate` timestamp NOT NULL DEFAULT current_timestamp(),
  `last_login` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `member`
--

INSERT INTO `member` (`member_id`, `email`, `password`, `nickname`, `member_level`, `amount_bought`, `has_shop`, `disabled`, `regdate`, `last_login`) VALUES
(1, 'aa@aa.com', '$s0$e1001$jnQryy/IiVX67YFWLHk5YA==$+DPCg6cphK/HiWHJUNUgW13zLdCxzdzx2TAzFt6ZLTU=', 'aabb', 1, 0, 1, 0, '2018-04-25 16:52:31', '2018-04-26 01:27:20'),
(2, 'aba@aba.com', '$s0$e1001$BiThhfO5lPr6CM5AjJj7ng==$00l2f+VaFxttD0mLiCVqwytWPAQ8WDUiLEACTKTYVBA=', 'aba', 1, 0, 1, 0, '2018-04-26 01:33:49', '2018-04-26 01:33:49'),
(3, 'eriChan@abmail.com', '$s0$e1001$jnQryy/IiVX67YFWLHk5YA==$+DPCg6cphK/HiWHJUNUgW13zLdCxzdzx2TAzFt6ZLTU=', 'Eric', 10, 0, 1, 0, '2018-04-11 21:30:27', '2018-04-25 07:30:11'),
(4, 'maryLam@citymail.com', '$s0$e1001$jnQryy/IiVX67YFWLHk5YA==$+DPCg6cphK/HiWHJUNUgW13zLdCxzdzx2TAzFt6ZLTU=', 'Mary', 5, 0, 1, 0, '2018-04-09 23:22:55', '2018-04-25 06:45:09'),
(5, 'kennyLo@abmail.com', '$s0$e1001$jnQryy/IiVX67YFWLHk5YA==$+DPCg6cphK/HiWHJUNUgW13zLdCxzdzx2TAzFt6ZLTU=', 'Kenny', 8, 0, 0, 0, '2018-04-14 18:25:18', '2018-04-25 03:20:52'),
(6, 'joetang@cmail.com', '$s0$e1001$jnQryy/IiVX67YFWLHk5YA==$+DPCg6cphK/HiWHJUNUgW13zLdCxzdzx2TAzFt6ZLTU=', 'Joe', 6, 0, 1, 0, '2018-04-08 04:31:44', '2018-04-24 08:30:50'),
(7, 'peterwong221@jmail.com', '$s0$e1001$jnQryy/IiVX67YFWLHk5YA==$+DPCg6cphK/HiWHJUNUgW13zLdCxzdzx2TAzFt6ZLTU=', 'Peter', 3, 0, 0, 0, '2018-04-21 06:40:28', '2018-04-24 04:13:19'),
(8, 'barryip@abmail.com', '$s0$e1001$jnQryy/IiVX67YFWLHk5YA==$+DPCg6cphK/HiWHJUNUgW13zLdCxzdzx2TAzFt6ZLTU=', 'Barry', 2, 0, 0, 0, '2018-04-21 17:19:06', '2018-04-25 18:30:25'),
(9, 'rosechan@cmail.com', '$s0$e1001$jnQryy/IiVX67YFWLHk5YA==$+DPCg6cphK/HiWHJUNUgW13zLdCxzdzx2TAzFt6ZLTU=', 'Rose', 1, 0, 0, 0, '2018-04-23 22:00:53', '2018-04-23 22:00:53'),
(10, 'jannylo@citymail.com', '$s0$e1001$jnQryy/IiVX67YFWLHk5YA==$+DPCg6cphK/HiWHJUNUgW13zLdCxzdzx2TAzFt6ZLTU=', 'Janny', 4, 0, 0, 0, '2018-04-15 21:30:34', '2018-04-18 21:55:20'),
(11, 'terryfung@cmail.com', '$s0$e1001$jnQryy/IiVX67YFWLHk5YA==$+DPCg6cphK/HiWHJUNUgW13zLdCxzdzx2TAzFt6ZLTU=', 'Terry', 9, 0, 1, 0, '2018-04-14 01:00:15', '2018-04-24 04:30:49'),
(12, 'hugocheung@abmail.com', '$s0$e1001$jnQryy/IiVX67YFWLHk5YA==$+DPCg6cphK/HiWHJUNUgW13zLdCxzdzx2TAzFt6ZLTU=', 'Hugo', 7, 0, 0, 0, '2018-04-15 19:38:42', '2018-04-23 05:25:00');

-- --------------------------------------------------------

--
-- 資料表結構 `product`
--

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `product_id` int(12) NOT NULL,
  `shop_id` int(12) NOT NULL DEFAULT 0,
  `product_name` varchar(255) NOT NULL DEFAULT '',
  `product_introduction` text NOT NULL,
  `product_detail` text NOT NULL,
  `product_category_id` int(12) NOT NULL DEFAULT 0,
  `product_price` double NOT NULL DEFAULT 0,
  `product_promotion_id` int(12) NOT NULL DEFAULT 0,
  `amount_stock` int(8) NOT NULL DEFAULT 0,
  `amount_sold` int(16) NOT NULL DEFAULT 0,
  `status` tinyint(2) NOT NULL DEFAULT 1,
  `added_on` timestamp NOT NULL DEFAULT current_timestamp(),
  `last_modified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `product`
--

INSERT INTO `product` (`product_id`, `shop_id`, `product_name`, `product_introduction`, `product_detail`, `product_category_id`, `product_price`, `product_promotion_id`, `amount_stock`, `amount_sold`, `status`, `added_on`, `last_modified`) VALUES
(1, 1, 'iPhone X', 'Phone', 'Apple phone', 1, 9999, 1, 2000, 0, 1, '2018-04-25 18:35:25', '2018-04-25 19:26:25'),
(2, 1, 'Iphone X 256GB', 'Lastest phone in apple.', ' New design of the phone, with 256GB storage. The new function of the phone which is face ID.', 3, 9888, 1, 8, 1, 1, '2018-04-14 22:01:35', '2018-04-14 22:01:35'),
(3, 2, 'Tempo Box Facial Tissue', 'Famous brand of tissue in Hong Kong', 'unKnown', 6, 33.5, 5, 10, 10, 1, '2018-04-09 21:01:36', '2018-04-09 21:01:36'),
(4, 3, 'Adidas FASHION LEAGUE TRACK JACKET', 'Adidas Brand.', 'unKnown', 1, 640, 3, 6, 0, 1, '2018-04-11 22:01:37', '2018-04-11 22:01:37'),
(5, 4, 'Ray-Ban RB4297 Square Sunglasses', 'Ray-Ban Brand.', 'unKnown', 2, 1650, 4, 5, 0, 1, '2018-04-12 23:01:38', '2018-04-12 23:01:38'),
(6, 1, 'Canon PowerShot SX430 IS', 'Brand: Canon', 'unKnown', 4, 1970, 4, 4, 2, 1, '2018-04-14 22:02:35', '2018-04-14 22:02:35'),
(7, 2, 'Dove Beauty Nourishing Shower Gel 750g', 'Brand: Dove', 'unKnown', 6, 38.9, 5, 5, 0, 1, '2018-04-09 21:02:36', '2018-04-09 21:02:36'),
(8, 3, 'Adidas Trefoil Hoodie', 'Brand: Adidas', 'unKnown', 1, 550, 1, 6, 0, 1, '2018-04-11 22:02:37', '2018-04-11 22:02:37'),
(9, 4, 'River Island Black Basic Belt', 'Brand: River Island', 'unKnown', 2, 142.9, 3, 8, 0, 1, '2018-04-12 23:02:38', '2018-04-12 23:02:38'),
(10, 1, ' JBL Flip 4', 'It is a portable speaker.', 'A full-featured waterproof portable Bluetooth speaker with surprisingly powerful sound.', 5, 784.8, 1, 5, 1, 1, '2018-04-14 22:03:35', '2018-04-14 22:03:35'),
(11, 2, 'Colgate Max White Toothpaste 160g', 'Brand: Colgate', 'unKnown', 6, 22.9, 1, 6, 0, 1, '2018-04-09 21:03:36', '2018-04-09 21:03:36'),
(12, 3, 'Adidas Yoga Seamless Space-Dye Tights', 'Brand:Adidas', 'unKnown', 1, 673, 5, 3, 0, 1, '2018-04-11 22:03:37', '2018-04-11 22:03:37'),
(13, 4, 'Herschel Roy RFID Wallet', 'Brand: Herschel', 'unKnown', 2, 267.7, 1, 2, 0, 1, '2018-04-12 23:03:38', '2018-04-12 23:03:38'),
(14, 1, 'Monster DNA Prp', 'Brand: Monster', 'unKnown', 5, 990, 2, 9, 0, 1, '2018-04-14 22:04:35', '2018-04-14 22:04:35'),
(15, 2, 'TRESemme Shampoo 900ml - Moisture Rich', 'Brand: TRESemme', 'unKnown', 6, 59.9, 5, 2, 2, 1, '2018-04-09 21:04:36', '2018-04-09 21:04:36'),
(16, 3, 'Adidas Ultra Primeknit Parley Tee ', 'Brand : Adidas', 'unKnown', 1, 497.95, 3, 3, 0, 1, '2018-04-11 22:04:37', '2018-04-11 22:04:37'),
(17, 4, 'Armani Black Leather Watch Gift Set AR80012', 'Brand: Armani', 'unKnown', 2, 2500, 5, 1, 0, 1, '2018-04-12 23:04:38', '2018-04-12 23:04:38'),
(18, 1, 'Nokia 8 Sirocco (6+128GB)', 'The lastest phone in Nokia', 'unKnown', 5, 5288, 2, 5, 0, 1, '2018-04-14 22:05:35', '2018-04-14 22:05:35'),
(19, 2, 'Panadol Cold And Flu Extra24s', 'Brand: Panadol', 'unKnown', 6, 83.9, 2, 6, 0, 1, '2018-04-09 21:05:36', '2018-04-09 21:05:36'),
(20, 3, 'Adidas ID Washed Pants', 'Brand : Adidas', 'unKnown', 1, 470, 4, 8, 1, 1, '2018-04-11 22:05:37', '2018-04-11 22:05:37'),
(21, 4, 'Herschel Elmer Cap', 'Brand: Herschel', 'unKnown', 2, 210, 5, 2, 0, 1, '2018-04-12 23:05:38', '2018-04-12 23:05:38');

-- --------------------------------------------------------

--
-- 資料表結構 `product_category`
--

DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category` (
  `product_category_id` int(12) NOT NULL,
  `product_category_name` varchar(64) NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `product_category`
--

INSERT INTO `product_category` (`product_category_id`, `product_category_name`) VALUES
(1, 'Smart Phone'),
(2, 'Computer'),
(3, 'Mouse'),
(4, 'Clothes'),
(5, 'Accessories'),
(6, 'Phone'),
(7, 'Camera'),
(8, 'Head Set / Speaker'),
(9, 'Convenience goods');

-- --------------------------------------------------------

--
-- 資料表結構 `product_comment`
--

DROP TABLE IF EXISTS `product_comment`;
CREATE TABLE `product_comment` (
  `product_id` int(12) NOT NULL DEFAULT 0,
  `member_id` int(12) NOT NULL DEFAULT 0,
  `comment_details` text NOT NULL,
  `product_rating` tinyint(10) NOT NULL DEFAULT 0,
  `status` tinyint(2) NOT NULL DEFAULT 1,
  `added_on` timestamp NOT NULL DEFAULT current_timestamp(),
  `last_modified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `product_comment`
--

INSERT INTO `product_comment` (`product_id`, `member_id`, `comment_details`, `product_rating`, `status`, `added_on`, `last_modified`) VALUES
(1, 1, 'Super computer ar...', 9, 0, '2018-04-25 19:12:46', '2018-04-25 19:32:18'),
(2, 1, 'powerful iphone! The Face ID is great!', 10, 1, '2018-04-25 07:30:11', '2018-04-25 07:30:11'),
(2, 3, 'This brand of tissue is great!', 8, 1, '2018-04-25 03:20:52', '2018-04-25 03:20:52'),
(3, 4, 'I love this design very much.', 6, 1, '2018-04-24 08:30:50', '2018-04-24 08:30:50'),
(4, 2, 'It\'s very cool!', 5, 1, '2018-04-25 06:45:09', '2018-04-25 06:45:09'),
(5, 6, 'This is a good camera!', 7, 1, '2018-04-25 18:30:25', '2018-04-25 18:30:25'),
(6, 9, 'The smell is good!', 9, 1, '2018-04-24 04:30:49', '2018-04-24 04:30:49'),
(7, 8, 'Good looking!', 8, 1, '2018-04-18 21:55:20', '2018-04-18 21:55:20'),
(9, 10, 'Have a good quality, the main point is waterproof!', 8, 1, '2018-04-23 05:25:00', '2018-04-23 05:25:00'),
(11, 4, 'Good Quality, comfortable.', 6, 1, '2018-04-24 08:30:50', '2018-04-24 08:30:50'),
(12, 1, 'The colour is great.', 8, 1, '2018-04-25 07:30:20', '2018-04-25 07:30:20'),
(13, 2, 'Powerful audio performance with massive bass response. Ships with two detachable cables—one with an inline remote.', 9, 1, '2018-04-25 06:45:09', '2018-04-25 06:45:09'),
(16, 4, 'Quality is good.Beautiful as well.', 4, 1, '2018-04-24 08:30:50', '2018-04-24 08:30:50'),
(17, 5, 'Support nokia.', 5, 1, '2018-04-24 04:13:19', '2018-04-24 04:13:19'),
(19, 9, 'Good looking! Better quality.', 10, 1, '2018-04-24 04:30:49', '2018-04-24 04:30:49'),
(20, 8, 'It\'s so sample.', 5, 1, '2018-04-18 21:55:20', '2018-04-18 21:55:20');

-- --------------------------------------------------------

--
-- 資料表結構 `product_photo`
--

DROP TABLE IF EXISTS `product_photo`;
CREATE TABLE `product_photo` (
  `product_id` int(12) NOT NULL DEFAULT 0,
  `product_photo_path` varchar(255) NOT NULL DEFAULT '',
  `status` tinyint(2) NOT NULL DEFAULT 1,
  `added_on` timestamp NOT NULL DEFAULT current_timestamp(),
  `last_modified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `product_photo`
--

INSERT INTO `product_photo` (`product_id`, `product_photo_path`, `status`, `added_on`, `last_modified`) VALUES
(1, 'img/products/1/1.jpg', 1, '2018-04-14 22:01:35', '2018-04-14 22:01:35'),
(1, 'img/products/1/2.jpg', 1, '2018-04-14 22:01:36', '2018-04-14 22:01:36'),
(2, 'img/products/2/1.jpg', 1, '2018-04-09 21:01:36', '2018-04-09 21:01:36'),
(2, 'img/products/2/2.jpg', 1, '2018-04-09 21:01:37', '2018-04-09 21:01:37'),
(3, 'img/products/3/1.jpg', 1, '2018-04-11 22:01:37', '2018-04-11 22:01:37'),
(3, 'img/products/3/2.jpg', 1, '2018-04-11 22:01:38', '2018-04-11 22:01:38'),
(4, 'img/products/4/1.jpg', 1, '2018-04-12 23:01:38', '2018-04-12 23:01:38'),
(4, 'img/products/4/2.jpg', 1, '2018-04-12 23:01:39', '2018-04-12 23:01:39'),
(5, 'img/products/5/1.jpg', 1, '2018-04-14 22:02:35', '2018-04-14 22:02:35'),
(5, 'img/products/5/2.jpg', 1, '2018-04-14 22:02:36', '2018-04-14 22:02:36'),
(6, 'img/products/6/1.jpg', 1, '2018-04-09 21:02:36', '2018-04-09 21:02:36'),
(6, 'img/products/6/2.jpg', 1, '2018-04-09 21:02:37', '2018-04-09 21:02:37'),
(7, 'img/products/7/1.jpg', 1, '2018-04-11 22:02:37', '2018-04-11 22:02:37'),
(7, 'img/products/7/2.jpg', 1, '2018-04-11 22:02:38', '2018-04-11 22:02:38'),
(8, 'img/products/8/1.jpg', 1, '2018-04-12 23:02:38', '2018-04-12 23:02:38'),
(8, 'img/products/8/2.jpg', 1, '2018-04-12 23:02:39', '2018-04-12 23:02:39'),
(9, 'img/products/9/1.jpg', 1, '2018-04-14 22:03:35', '2018-04-14 22:03:35'),
(9, 'img/products/9/2.jpg', 1, '2018-04-14 22:03:36', '2018-04-14 22:03:36'),
(10, 'img/products/10/1.jpg', 1, '2018-04-09 21:03:36', '2018-04-09 21:03:36'),
(10, 'img/products/10/2.jpg', 1, '2018-04-09 21:03:37', '2018-04-09 21:03:37'),
(11, 'img/products/11/1.jpg', 1, '2018-04-11 22:03:37', '2018-04-11 22:03:37'),
(11, 'img/products/11/2.jpg', 1, '2018-04-11 22:03:38', '2018-04-11 22:03:38'),
(12, 'img/products/12/1.jpg', 1, '2018-04-12 23:03:38', '2018-04-12 23:03:38'),
(12, 'img/products/12/2.jpg', 1, '2018-04-12 23:03:39', '2018-04-12 23:03:39'),
(13, 'img/products/13/1.jpg', 1, '2018-04-14 22:04:35', '2018-04-14 22:04:35'),
(13, 'img/products/13/2.jpg', 1, '2018-04-14 22:04:36', '2018-04-14 22:04:36'),
(14, 'img/products/14/1.jpg', 1, '2018-04-09 21:04:36', '2018-04-09 21:04:36'),
(14, 'img/products/14/2.jpg', 1, '2018-04-09 21:04:37', '2018-04-09 21:04:37'),
(15, 'img/products/15/1.jpg', 1, '2018-04-11 22:04:37', '2018-04-11 22:04:37'),
(15, 'img/products/15/2.jpg', 1, '2018-04-11 22:04:38', '2018-04-11 22:04:38'),
(16, 'img/products/16/1.jpg', 1, '2018-04-12 23:04:38', '2018-04-12 23:04:38'),
(16, 'img/products/16/2.jpg', 1, '2018-04-12 23:04:39', '2018-04-12 23:04:39'),
(17, 'img/products/17/1.jpg', 1, '2018-04-14 22:05:35', '2018-04-14 22:05:35'),
(17, 'img/products/17/2.jpg', 1, '2018-04-14 22:05:36', '2018-04-14 22:05:36'),
(18, 'img/products/18/1.jpg', 1, '2018-04-09 21:05:36', '2018-04-09 21:05:36'),
(18, 'img/products/18/2.jpg', 1, '2018-04-09 21:05:37', '2018-04-09 21:05:37'),
(19, 'img/products/19/1.jpg', 1, '2018-04-11 22:05:37', '2018-04-11 22:05:37'),
(19, 'img/products/19/2.jpg', 1, '2018-04-11 22:05:38', '2018-04-11 22:05:38'),
(20, 'img/products/20/1.jpg', 1, '2018-04-12 23:05:38', '2018-04-12 23:05:38'),
(20, 'img/products/20/2.jpg', 1, '2018-04-12 23:05:39', '2018-04-12 23:05:39');

-- --------------------------------------------------------

--
-- 資料表結構 `product_product_tab`
--

DROP TABLE IF EXISTS `product_product_tab`;
CREATE TABLE `product_product_tab` (
  `product_id` int(12) NOT NULL DEFAULT 0,
  `product_tab_id` int(12) NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `product_product_tab`
--

INSERT INTO `product_product_tab` (`product_id`, `product_tab_id`) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 1),
(2, 2),
(3, 1),
(4, 1),
(5, 1),
(6, 1),
(7, 1),
(8, 1),
(9, 1),
(10, 1),
(11, 1),
(12, 1),
(13, 1),
(14, 1),
(15, 1),
(16, 1),
(17, 1),
(18, 1),
(19, 1),
(20, 1);

-- --------------------------------------------------------

--
-- 資料表結構 `product_product_type`
--

DROP TABLE IF EXISTS `product_product_type`;
CREATE TABLE `product_product_type` (
  `product_id` int(12) NOT NULL,
  `product_type_id` int(12) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `product_product_type`
--

INSERT INTO `product_product_type` (`product_id`, `product_type_id`) VALUES
(1, 1),
(2, 1),
(2, 2),
(3, 1),
(3, 3),
(4, 1),
(4, 2),
(5, 1),
(5, 2),
(6, 1),
(7, 1),
(8, 1),
(8, 3),
(9, 1),
(9, 3),
(10, 1),
(11, 1),
(11, 3),
(12, 1),
(13, 1),
(13, 2),
(14, 1),
(15, 1),
(15, 3),
(16, 1),
(17, 1),
(18, 1),
(19, 1),
(20, 1);

-- --------------------------------------------------------

--
-- 資料表結構 `product_promotion`
--

DROP TABLE IF EXISTS `product_promotion`;
CREATE TABLE `product_promotion` (
  `product_promotion_id` int(12) NOT NULL,
  `product_promotion_name` varchar(64) NOT NULL DEFAULT '',
  `product_promotion_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0=No promotion, 1=by persentage, 2=minus a price',
  `product_promotion_amount` int(12) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `product_promotion`
--

INSERT INTO `product_promotion` (`product_promotion_id`, `product_promotion_name`, `product_promotion_type`, `product_promotion_amount`) VALUES
(1, 'New', 1, 1),
(2, 'New product', 1, 1),
(3, 'New product', 1, 1),
(4, 'VIP promotion', 2, 5),
(5, 'New shop', 2, 10),
(6, 'Sell', 1, 1);

-- --------------------------------------------------------

--
-- 資料表結構 `product_tab`
--

DROP TABLE IF EXISTS `product_tab`;
CREATE TABLE `product_tab` (
  `product_tab_id` int(12) NOT NULL,
  `product_tab_name` varchar(32) NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `product_tab`
--

INSERT INTO `product_tab` (`product_tab_id`, `product_tab_name`) VALUES
(1, 'phone'),
(2, 'Apple'),
(3, 'new'),
(4, 'Apple,IphoneX,256GB,dual camera'),
(5, 'Tempo,Tissue,Convenience goods'),
(6, 'Adidas, Jacket, Clothes'),
(7, 'Ray-Ban, sunglasses, Accessories'),
(8, 'Canon, Camera'),
(9, 'Dove, Convenience goods,Shower G'),
(10, 'Adidas, Hoodies, Clothes'),
(11, 'River Island, Belt, Accessories'),
(12, 'JBL, audio, portable, speaker,wa'),
(13, 'Colgate, Toothpaste, Convenience'),
(14, 'Adidas, Yoga, Seamless, Clothes'),
(15, 'Herschel, Wallet, Accessories'),
(16, 'Monster,audio ,headphone'),
(17, 'TRESemme, Shampoo, Convenience g'),
(18, 'Adidas, Tee, Clothes'),
(19, 'Armani, Watch, Accessories'),
(20, 'Nokia,Phone , 8 Sirocco, Phone, '),
(21, 'Panadol, Medicine, Convenience g'),
(22, 'Adidas, Pant, Male, Clothes'),
(23, 'Herschel, Cap, Accessories');

-- --------------------------------------------------------

--
-- 資料表結構 `product_type`
--

DROP TABLE IF EXISTS `product_type`;
CREATE TABLE `product_type` (
  `product_type_id` int(12) NOT NULL,
  `product_type_name` varchar(32) NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `product_type`
--

INSERT INTO `product_type` (`product_type_id`, `product_type_name`) VALUES
(1, 'red'),
(2, 'blue'),
(3, 'black');

-- --------------------------------------------------------

--
-- 資料表結構 `shop`
--

DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
  `shop_id` int(12) NOT NULL,
  `shop_name` varchar(64) NOT NULL DEFAULT '',
  `shop_introduction` text NOT NULL,
  `shop_detail` text NOT NULL,
  `shop_category_id` int(12) NOT NULL DEFAULT 0,
  `shop_level` tinyint(2) NOT NULL DEFAULT 1,
  `member_id` int(12) NOT NULL DEFAULT 0,
  `contact_number` int(8) NOT NULL DEFAULT 0,
  `contact_email` varchar(255) NOT NULL DEFAULT '',
  `website` varchar(255) NOT NULL DEFAULT '',
  `amount_sold` int(16) NOT NULL DEFAULT 0,
  `disabled` tinyint(1) NOT NULL DEFAULT 0,
  `created_on` timestamp NOT NULL DEFAULT current_timestamp(),
  `last_modified` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `shop`
--

INSERT INTO `shop` (`shop_id`, `shop_name`, `shop_introduction`, `shop_detail`, `shop_category_id`, `shop_level`, `member_id`, `contact_number`, `contact_email`, `website`, `amount_sold`, `disabled`, `created_on`, `last_modified`) VALUES
(1, 'AA Shop', 'Hi, everyone.', 'Come to HERE!!', 2, 1, 1, 123456, 'aa@aa.com', 'aa.com', 0, 0, '2018-04-25 18:10:56', '2018-04-25 18:10:56'),
(2, 'A Shop', 'Shop', 'Shop', 1, 1, 2, 123, 'ww@ww.com', 'ww.com', 0, 0, '2018-04-26 01:34:51', '2018-04-26 01:34:51'),
(3, 'Happy Electronic', 'We are selling some electronic product.', 'Our store will sell high quality electronic products, just like iphone, cannon camera.', 1, 9, 9, 26665551, 'happyelec@citymail.com', 'happyelectronic.com.hk', 0, 1, '2018-04-14 22:00:35', '2018-04-24 04:35:49'),
(4, 'Jasper SuperMarket', 'We are selling some convenience goods.', 'Our store sell a lot of different convenience goods.', 2, 7, 4, 23456789, 'jaspersm@citymail.com', 'jaspershop.com.hk', 0, 1, '2018-04-09 21:00:36', '2018-04-24 08:35:50'),
(5, 'Nano Fashion', 'We are selling some clothes.', 'Our store will sell clothes, ', 3, 5, 2, 33126659, 'nanofashion@citymail.com', 'nanofashion.com.hk', 0, 1, '2018-04-11 22:00:37', '2018-04-25 06:50:09'),
(6, 'Buy Ur Faviour', 'We are selling some accessories.', 'Our store sell a lot of different accessories.', 4, 10, 1, 22951675, 'buyuf@citymail.com', 'buyourfaviour.com.hk', 0, 1, '2018-04-12 23:00:38', '2018-04-25 07:35:11');

-- --------------------------------------------------------

--
-- 資料表結構 `shop_category`
--

DROP TABLE IF EXISTS `shop_category`;
CREATE TABLE `shop_category` (
  `shop_category_id` int(12) NOT NULL,
  `shop_category_name` varchar(64) NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `shop_category`
--

INSERT INTO `shop_category` (`shop_category_id`, `shop_category_name`) VALUES
(1, 'Smart phone'),
(2, 'Smart watch'),
(3, 'Electronic Products'),
(4, 'Convenience goods'),
(5, 'Clothing'),
(6, 'Organic Product');

-- --------------------------------------------------------

--
-- 資料表結構 `shop_shop_tab`
--

DROP TABLE IF EXISTS `shop_shop_tab`;
CREATE TABLE `shop_shop_tab` (
  `shop_id` int(12) NOT NULL,
  `shop_tab_id` int(12) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `shop_shop_tab`
--

INSERT INTO `shop_shop_tab` (`shop_id`, `shop_tab_id`) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 1),
(2, 2),
(2, 4),
(3, 3),
(4, 4);

-- --------------------------------------------------------

--
-- 資料表結構 `shop_tab`
--

DROP TABLE IF EXISTS `shop_tab`;
CREATE TABLE `shop_tab` (
  `shop_tab_id` int(12) NOT NULL,
  `shop_tab_name` varchar(32) NOT NULL DEFAULT ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- 資料表的匯出資料 `shop_tab`
--

INSERT INTO `shop_tab` (`shop_tab_id`, `shop_tab_name`) VALUES
(1, 'Apple'),
(2, 'Samsung'),
(3, 'Google'),
(4, 'aa'),
(5, 'Electronic, Phone, headphone, sp'),
(6, 'Convenience goods'),
(7, 'Clothes, sport, Adidas, Nike'),
(8, 'Organic, fruit, vegetable ');

--
-- 已匯出資料表的索引
--

--
-- 資料表索引 `buyer_order`
--
ALTER TABLE `buyer_order`
  ADD PRIMARY KEY (`buyer_order_id`);

--
-- 資料表索引 `buyer_order_product`
--
ALTER TABLE `buyer_order_product`
  ADD PRIMARY KEY (`buyer_order_id`,`product_id`,`product_type_id`);

--
-- 資料表索引 `buyer_order_promotion`
--
ALTER TABLE `buyer_order_promotion`
  ADD PRIMARY KEY (`buyer_order_promotion_id`);

--
-- 資料表索引 `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`member_id`,`product_id`,`product_type_id`);

--
-- 資料表索引 `member`
--
ALTER TABLE `member`
  ADD PRIMARY KEY (`member_id`);

--
-- 資料表索引 `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`product_id`);

--
-- 資料表索引 `product_category`
--
ALTER TABLE `product_category`
  ADD PRIMARY KEY (`product_category_id`);

--
-- 資料表索引 `product_comment`
--
ALTER TABLE `product_comment`
  ADD PRIMARY KEY (`product_id`,`member_id`);

--
-- 資料表索引 `product_photo`
--
ALTER TABLE `product_photo`
  ADD PRIMARY KEY (`product_id`,`product_photo_path`);

--
-- 資料表索引 `product_product_tab`
--
ALTER TABLE `product_product_tab`
  ADD PRIMARY KEY (`product_id`,`product_tab_id`);

--
-- 資料表索引 `product_product_type`
--
ALTER TABLE `product_product_type`
  ADD PRIMARY KEY (`product_id`,`product_type_id`);

--
-- 資料表索引 `product_promotion`
--
ALTER TABLE `product_promotion`
  ADD PRIMARY KEY (`product_promotion_id`);

--
-- 資料表索引 `product_tab`
--
ALTER TABLE `product_tab`
  ADD PRIMARY KEY (`product_tab_id`);

--
-- 資料表索引 `product_type`
--
ALTER TABLE `product_type`
  ADD PRIMARY KEY (`product_type_id`);

--
-- 資料表索引 `shop`
--
ALTER TABLE `shop`
  ADD PRIMARY KEY (`shop_id`);

--
-- 資料表索引 `shop_category`
--
ALTER TABLE `shop_category`
  ADD PRIMARY KEY (`shop_category_id`);

--
-- 資料表索引 `shop_shop_tab`
--
ALTER TABLE `shop_shop_tab`
  ADD PRIMARY KEY (`shop_id`,`shop_tab_id`);

--
-- 資料表索引 `shop_tab`
--
ALTER TABLE `shop_tab`
  ADD PRIMARY KEY (`shop_tab_id`);

--
-- 在匯出的資料表使用 AUTO_INCREMENT
--

--
-- 使用資料表 AUTO_INCREMENT `buyer_order`
--
ALTER TABLE `buyer_order`
  MODIFY `buyer_order_id` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- 使用資料表 AUTO_INCREMENT `buyer_order_promotion`
--
ALTER TABLE `buyer_order_promotion`
  MODIFY `buyer_order_promotion_id` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- 使用資料表 AUTO_INCREMENT `member`
--
ALTER TABLE `member`
  MODIFY `member_id` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- 使用資料表 AUTO_INCREMENT `product`
--
ALTER TABLE `product`
  MODIFY `product_id` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- 使用資料表 AUTO_INCREMENT `product_category`
--
ALTER TABLE `product_category`
  MODIFY `product_category_id` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- 使用資料表 AUTO_INCREMENT `product_promotion`
--
ALTER TABLE `product_promotion`
  MODIFY `product_promotion_id` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- 使用資料表 AUTO_INCREMENT `product_tab`
--
ALTER TABLE `product_tab`
  MODIFY `product_tab_id` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- 使用資料表 AUTO_INCREMENT `product_type`
--
ALTER TABLE `product_type`
  MODIFY `product_type_id` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- 使用資料表 AUTO_INCREMENT `shop`
--
ALTER TABLE `shop`
  MODIFY `shop_id` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- 使用資料表 AUTO_INCREMENT `shop_category`
--
ALTER TABLE `shop_category`
  MODIFY `shop_category_id` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- 使用資料表 AUTO_INCREMENT `shop_tab`
--
ALTER TABLE `shop_tab`
  MODIFY `shop_tab_id` int(12) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
