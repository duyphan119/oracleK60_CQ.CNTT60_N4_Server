package com.api.shoesshop.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.api.shoesshop.entities.Order;
import com.api.shoesshop.entities.OrderItem;
import com.api.shoesshop.entities.ProductVariant;
import com.api.shoesshop.interceptors.AuthInterceptor;
import com.api.shoesshop.repositories.OrderItemRepository;
import com.api.shoesshop.repositories.OrderRepository;
import com.api.shoesshop.repositories.ProductDetailRepository;
import com.api.shoesshop.services.OrderService;
import com.api.shoesshop.types.FindAll;
import com.api.shoesshop.utils.Helper;

@Controller

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	ProductDetailRepository productVariantRepository;

	@GetMapping("/api/order")
	public ResponseEntity<String> findAll(HttpServletRequest req, @RequestParam Map<String, String> query) {
		if (AuthInterceptor.isAdmin(req) == true) {
			try {
				String fullName = query.get("full_name");
				String phone = query.get("phone");
				String province = query.get("province");
				String district = query.get("district");
				String ward = query.get("ward");
				String address = query.get("address");
				String begin = query.get("begin");
				String end = query.get("end");
				String sql = "select order_id from orders where order_status is not null";
				if (begin != null) {
					sql += " and created_at >= To_date(TO_CHAR(TO_DATE('" + begin
							+ "','yyyy-MM-dd'),'dd-Mon-yy'),'dd-Mon-yy')";
				}
				if (end != null) {
					sql += " and created_at <= To_date(TO_CHAR(TO_DATE('" + end
							+ "','yyyy-MM-dd'),'dd-Mon-yy'),'dd-Mon-yy')";
				}
				if (phone != null) {
					sql += " and phone like '%" + phone + "%'";
				}
				if (fullName != null) {
					sql += " and full_name like '%" + fullName + "%'";
				}
				if (province != null) {
					sql += " and province like '%" + province + "%'";
				}
				if (district != null) {
					sql += " and district like '%" + district + "%'";
				}
				if (ward != null) {
					sql += " and ward like '%" + ward + "%'";
				}
				if (address != null) {
					sql += " and address like '%" + address + "%'";
				}
				List<Long> ids = jdbcTemplate.query(sql, new RowMapper<Long>() {
					public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getLong("order_id");
					}
				});
				Page<Order> page = orderRepository.findByIdIn(ids, Helper.getPageable(query));
				return Helper.responseSuccess(
						new FindAll<>(page.getContent(), page.getTotalElements()));
			} catch (Exception e) {
				System.out.println(e);
				return Helper.responseError();
			}
		}
		return Helper.responseUnauthorized();
	}

	@GetMapping("/api/order/account")
	public ResponseEntity<String> findByAccount(HttpServletRequest req, @RequestParam Map<String, String> query) {
		if (AuthInterceptor.isLoggedin(req) == true) {
			try {
				long accountId = Long.parseLong(req.getAttribute("account_id").toString());
				String begin = query.get("begin");
				String end = query.get("end");
				String sql = "select order_id from orders where account_id_pk=" + accountId
						+ " and order_status is not null";
				if (begin != null) {
					sql += " and created_at >= To_date(TO_CHAR(TO_DATE('" + begin
							+ "','yyyy-MM-dd'),'dd-Mon-yy'),'dd-Mon-yy')";
				}
				if (end != null) {
					sql += " and created_at <= To_date(TO_CHAR(TO_DATE('" + end
							+ "','yyyy-MM-dd'),'dd-Mon-yy'),'dd-Mon-yy')";
				}
				List<Long> ids = jdbcTemplate.query(sql, new RowMapper<Long>() {
					public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getLong("order_id");
					}
				});
				Page<Order> page = orderRepository.findByIdIn(ids, Helper.getPageable(query));
				return Helper.responseSuccess(
						new FindAll<>(page.getContent(), page.getTotalElements()));
			} catch (Exception e) {
				System.out.println(e);
				return Helper.responseError();
			}
		}
		return Helper.responseUnauthorized();
	}

	@GetMapping("/api/order/{id}")
	public ResponseEntity<String> findById(HttpServletRequest req, @PathVariable Long id) {
		if (AuthInterceptor.isAdmin(req) == true) {
			try {
				Order order = orderService.findById(id);
				if (order != null)
					return Helper.responseSuccess(order);

				return Helper.responseError();
			} catch (Exception e) {
				System.out.println(e);
				return Helper.responseError();
			}
		}
		return Helper.responseUnauthorized();
	}

	@PostMapping("/api/order/account")
	public ResponseEntity<String> save(HttpServletRequest req, @RequestBody Order body) {
		if (AuthInterceptor.isCustomer(req) == true) {
			try {
				if (req.getAttribute("account_id") != null) {
					long accountId = Long.parseLong(req.getAttribute("account_id").toString());
					Order order = orderService.save(accountId, body);
					if (order != null)
						return Helper.responseCreated(order);
				}
				return Helper.responseUnauthorized();
			} catch (Exception e) {
				System.out.println(e);
				return Helper.responseError();
			}
		}
		return Helper.responseUnauthorized();
	}

	@PostMapping("/api/order")
	public ResponseEntity<String> saveByAdmin(HttpServletRequest req, @RequestBody Order body) {
		if (AuthInterceptor.isAdmin(req) == true) {
			try {
				Order order = orderService.save(body.getAccountId(), body);
				if (order != null)
					return Helper.responseCreated(order);
				return Helper.responseUnauthorized();
			} catch (Exception e) {
				System.out.println(e);
				return Helper.responseError();
			}
		}
		return Helper.responseUnauthorized();
	}

	@PatchMapping("/api/order/{id}")
	public ResponseEntity<String> update(HttpServletRequest req, @RequestBody Order body, @PathVariable Long id) {
		if (AuthInterceptor.isAdmin(req) == true) {
			try {
				Optional<Order> optional = orderRepository.findById(id);
				if (optional.isPresent() == true && body.getStatus().equals("Đang xử lý") == false
						&& optional.get().getStatus().equals("Đang xử lý") == true) {
					Order order = optional.get();
					Set<OrderItem> items = order.getItems();
					Iterator<OrderItem> iterator = items.iterator();
					boolean isOk = true;
					while (iterator.hasNext() == true) {

						OrderItem item = iterator.next();
						ProductVariant pv = item.getProductVariant();
						if (item.getQuantity() > pv.getInventory()) {
							isOk = false;
							break;
						}
					}
					if (isOk == true) {
						order.setStatus(body.getStatus());
						Order order1 = orderRepository.save(order);
						if (order1 != null)
							return Helper.responseSuccess(order1);
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			return Helper.responseError();
		}
		return Helper.responseUnauthorized();
	}

	@PatchMapping("/api/order/account/payment")
	public ResponseEntity<String> accountPayment(HttpServletRequest req, @RequestBody Order body) {
		if (AuthInterceptor.isCustomer(req) == true) {
			try {
				long accountId = Long.parseLong(req.getAttribute("account_id").toString());
				Order order = orderRepository.findByStatusAndAccountId(null, accountId);
				if (order != null) {
					order.setAddress(body.getAddress());
					order.setStatus("Đang xử lý");
					Set<OrderItem> items = order.getItems();
					Iterator<OrderItem> iterator = items.iterator();
					while (iterator.hasNext() == true) {
						OrderItem item = iterator.next();
						item.setPrice(items.iterator().next().getProductVariant().getProduct().getSalePrice());
						// ProductVariant pv = item.getProductVariant();
						// pv.setInventory(pv.getInventory() - item.getQuantity());
						// productVariantRepository.save(pv);
						orderItemRepository.save(item);
					}
					order.setCreatedAt(new Date());
					order.setFullName(body.getFullName());
					order.setPhone(body.getPhone());
					order.setCouponId(body.getCouponId());
					order.setPaymentMethod(body.getPaymentMethod());
					return Helper.responseSuccess(orderRepository.save(order));
				}

				return Helper.responseUnauthorized();
			} catch (Exception e) {
				System.out.println(e);
				return Helper.responseError();
			}
		}
		return Helper.responseUnauthorized();
	}

	@DeleteMapping("/api/order/{id}")
	public ResponseEntity<String> update(HttpServletRequest req, @PathVariable Long id) {
		try {
			if (AuthInterceptor.isCustomer(req) == true) {
				Order order = orderService.findById(id);
				if (order != null && order.getAccountId() == Long
						.parseLong(req.getAttribute("account_id").toString())
						&& order.getStatus().indexOf("ang x") != -1) {

					Set<OrderItem> items = order.getItems();
					Iterator<OrderItem> iterator = items.iterator();
					while (iterator.hasNext() == true) {
						OrderItem item = iterator.next();
						ProductVariant pv = item.getProductVariant();
						pv.setInventory(pv.getInventory() + item.getQuantity());
						productVariantRepository.save(pv);
					}
					jdbcTemplate.execute("delete from orders where order_id=" + id);
					// orderService.delete(id);
					return Helper.responseSuccessNoData();
				}

			}
			if (AuthInterceptor.isAdmin(req) == true) {
				Order order = orderService.findById(id);
				if (order != null && order.getStatus().indexOf("ang x") != -1) {
					Set<OrderItem> items = order.getItems();
					Iterator<OrderItem> iterator = items.iterator();
					while (iterator.hasNext() == true) {
						OrderItem item = iterator.next();
						ProductVariant pv = item.getProductVariant();
						pv.setInventory(pv.getInventory() + item.getQuantity());
						productVariantRepository.save(pv);
					}
					jdbcTemplate.execute("delete from orders where order_id=" + id);
					// orderService.delete(id);
					return Helper.responseSuccessNoData();
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			return Helper.responseError();
		}
		return Helper.responseUnauthorized();
	}
}
