(let ((a!1 (or POL_1.P
               (and POL_1.NA POL_2.P)
               (and (and POL_1.NA POL_2.NA) POL_3.P)
               (and (and (and POL_1.NA POL_2.NA) POL_3.NA) POL_4.P)
               (and (and (and POL_1.NA POL_2.NA) POL_3.NA) POL_4.NA POL_5.P))))
  (and (or (and (select resource-type Patient.Record) (select action-type Read)))
       (not false)
       a!1))

Deny (let ((a!1 (or POL_1.D
               (and POL_1.NA POL_2.D)
               (and (and POL_1.NA POL_2.NA) POL_3.D)
               (and (and (and POL_1.NA POL_2.NA) POL_3.NA) POL_4.D)
               (and (and (and POL_1.NA POL_2.NA) POL_3.NA) POL_4.NA POL_5.D))))
  (and (or (and (select resource-type Patient.Record) (select action-type Read)))
       (not false)
       a!1))

(let ((a!1 (and (or (and (select resource-type Patient.Record)
                         (select action-type Read))))))
  (and (or (not a!1) (and POL_1.NA POL_2.NA POL_3.NA POL_4.NA POL_5.NA))
       (not false)))

Na (or (not (and (select resource-type Patient.Record) (select action-type Read)))
    (and POL_1.NA POL_2.NA POL_3.NA POL_4.NA POL_5.NA))

Indet (and (select resource-type Patient.Record)
     (select action-type Read)
     (or POL_1.IN_PD
         POL_1.IN_P
         POL_1.IN_D
         (and POL_1.NA (or POL_2.IN_PD POL_2.IN_P POL_2.IN_D))
         (and POL_1.NA POL_2.NA (or POL_3.IN_PD POL_3.IN_P POL_3.IN_D))
         (and POL_1.NA POL_2.NA POL_3.NA (or POL_4.IN_PD POL_4.IN_P POL_4.IN_D))
         (and POL_1.NA
              POL_2.NA
              POL_3.NA
              POL_4.NA
              (or POL_5.IN_PD POL_5.IN_P POL_5.IN_D))))

Deny (and (select resource-type Patient.Record)
     (select action-type Read)
     (or POL_1.D
         (and POL_1.NA POL_2.D)
         (and POL_1.NA POL_2.NA POL_3.D)
         (and POL_1.NA POL_2.NA POL_3.NA POL_4.D)
         (and POL_1.NA POL_2.NA POL_3.NA POL_4.NA POL_5.D)))

Permit (and (select resource-type Patient.Record)
     (select action-type Read)
     (or POL_1.P
         (and POL_1.NA POL_2.P)
         (and POL_1.NA POL_2.NA POL_3.P)
         (and POL_1.NA POL_2.NA POL_3.NA POL_4.P)
         (and POL_1.NA POL_2.NA POL_3.NA POL_4.NA POL_5.P)))
	
	Mình có thể trình bày ý tưởng của mình trong bài báo và submit ngay
		Để có 1 sự đánh giá/góp ý thú vị cho hướng phát triển kế tiếp của mình?

	Vậy viết bài báo thôi
		Cố lên!!!!
		Bài báo giới thiệu 2 bài toán thú vị
			Áp dụng Dynamic Policy để xử lý bài toán Break The Glass
			Tạo Policy dành cho người dùng không chuyên

	Nếu mình tách Pol.Target và Pol.Comb
		Mình có thể loại trừ những Policy không liên quan ngay.
			Đối với một nhóm request

	Giả sử thêm mới POL_5
		Và muốn thỏa mãn 1 request

	Nếu mình có thể thủ nghiệm được trên các bộ policy đang có.
		Continue A

Cái IN_PD, IN_D và IN_P hơi gây nhiễu trong việc phân tích
	Có thể thu giảm như thế nào?

Trong khi phân tích
	Ta thường quan tâm Permit, Deny và NA là chính?
	Làm sao để tăng Permit:
		Không gian Permit của RuleX xuất hiện trong biểu thức
			=> Có thể mở rộng thêm không gian này.
	Làm sao để tăng Deny:

	Vấn đề ở đây cũng phụ thuộc vào RCA
		Đang dùng Deny Override
			Để Permit yêu cầu một sự phức tạp nhất định trong biểu thức
			Deny rất rõ ràng

Định hướng sự thay đổi của Policy như thế nào?
Hơi khó định hướng
	Thay đổi bên trong DS của Rule
	Thay đổi sự kết hợp của các rule

Mình có biểu thức hiển thị ở các cấp độ khác nhau
	Cho phép query ở nhiều mức
		Thay thế càng lúc càng nhiều hơn
			Thu giảm biểu thức về dạng đơn giản

			=> Khi muốn thay đổi:
				Có thể dựa trên biểu thức đang có được hay không?
					Thêm các constraint mới vào biểu thức đang có
						=> Làm sao quay về biểu thức ban đầu?
					Thêm các Rule.DS vào
						Có thể coi đó là các Rule nhưng chưa biết nội dung
							Mình sẽ fullfill nội dung sau đó
	=> Giả sử muốn thêm 1 policy mới
		=> Mình có thể thêm 1 Policy rỗng
			Rồi làm rõ cho policy đó
				=> Khi thêm 1 policy rỗng thì sẽ như thế nào?
					Có thể xuất phát từ việc New_Pol.P luôn là true
			Có thể tách Policy thành Pol.Target và Pol.Comb
				Pol.P, Pol.D, Pol.Other có liên quan gì đến Pol.Target và Pol.Comb?
					Đã có công thức ở trong code.
						Vậy 1 Pol sẽ cấu thành từ
							Pol.Target
							Pol.Comb group

	=> Hoặc muốn thêm 1 rule mới

Mình có 1 công thức tổng quát

Mình có 1 công thức có apply effect => các IN sẽ bị thu giảm

Mình có 1 công thức có apply luôn các false không gian

Mình có 1 công thức thay thế điều kiện 1 request
	Sự pha tạp?
		Nên như thế nào?
		Theo hướng mở rộng cho request
			Biểu thức thể hiện các Rule được apply
				Thể hiện toàn bộ các DS các Rule

				Thể hiện các R.DS khác false apply cho request

				Biểu thức dựa trên các AC


			=> false các vùng không gian khác

Nếu muốn thay đổi thì nên làm gì?
	Policy vẫn là một khu vực mờ ảo, khó hiểu khi trở nên phức tạp.

	Lần đầu thể hiện một cách tường minh
		1 bộ policy
			Có thể hiểu và phân tích
	Thay đổi sự kết hợp giữa các Policy
	Thêm Policy mới
	Sửa Policy
		Thêm Rule mới
		Thay đổi sự kết hợp giữa các Rule
		Thay đổi Rule.DS
			=> Thay đổi AC
			=> Thêm AC
			=> Remove AC
	Xóa Policy

	Định hướng cho sự thay đổi?

	Ngữ nghĩa của sự thay đổi?

	Ảnh hưởng của sự thay đổi
		Nếu thêm 1 policy => Vùng không gian của 1 nhóm request sẽ bị thay đổi
			Có thể thêm các của Rule.DS của policy mới này hoặc không.
				Thể hiện biểu thức sau cùng
					Với các thay thế
		Đánh giá 1 cách tương đối

	Có những dạng mô hình gì khi kết hợp các Rule hoặc Policy không?

	Chẳng giúp được gì cả
	Điều gì giúp mình tiến lên vậy?
		Nếu với cách làm việc của mình thì vẫn có những thứ giúp mình tiến tới được!
			Mình cần các cái mẫu gần gần

Permit (goal
  (select |patient:management-info:status| AvailableNurseDoctor)
  (or (select role Doctor) (select role Nurse))
  (or R_1.P R_2.P R_3.P)
  (not R_1.D)
  (not R_2.D)
  (not R_3.D)
  (not (and (or R_1.IN_PD R_2.IN_PD R_1.IN_D R_2.IN_D) (not (or R_1.D R_2.D))))
  (not R_3.IN_PD)
  (not R_3.IN_D))

Deny (and (select |patient:management-info:status| AvailableNurseDoctor)
     (or (select role Doctor) (select role Nurse))
     (or R_1.D R_2.D R_3.D))

Permit (let ((a!1 (and (or R_1.IN_PD
                    R_2.IN_PD
                    (and R_1.IN_D (or R_2.IN_P R_2.P)))))))

                    (and R_2.IN_D (or R_1.IN_P R_1.P)))
                (not (or R_1.D R_2.D(let ((a!2 (and (or R_1.IN_D R_2.IN_D) (not (or R_1.D R_2.D)) (not a!1))))
(let ((a!3 (and (or R_1.P R_2.P) (not (or R_1.D R_2.D)) (not a!1) (not a!2))))
(let ((a!4 (or (and (or R_1.IN_P R_2.IN_P)
                    (not (or R_1.D R_2.D))
                    (not a!1)
                    (not a!2)
                    (not a!3))
               a!3)))
(let ((a!5 (or a!1
               R_3.IN_PD
               (and (or R_1.IN_D R_2.IN_D)
                    (not (or R_1.D R_2.D))
                    (not a!1)
                    (or R_3.IN_P R_3.P))
               (and R_3.IN_D a!4))))
(let ((a!6 (not (and a!5 (not (or R_1.D R_2.D R_3.D))))))
(let ((a!7 (not (and (or a!2 R_3.IN_D) (not (or R_1.D R_2.D R_3.D)) a!6))))
  (and (select |patient:management-info:status| AvailableNurseDoctor)
       (or (select role Doctor) (select role Nurse))
       (or a!3 R_3.P)
       (not (or R_1.D R_2.D R_3.D))
       a!6
       a!7))))))))


Deny (goal
  (select resource-type Patient.Record)
  (let ((a!1 (not (and (select |patient:management-info:status|
                               AvailableNurseDoctor)
                       (or (select role Doctor) (select role Nurse)))))
        (a!3 (or (not (and (select role External) (select party-type Insurance)))
                 POL_2.Comb.NA))
        (a!4 (or (not (and (select role External) (select party-type Pharmacy)))
                 POL_3.Comb.NA))
        (a!6 (or (not (or (select |patient:management-info:conscious-status|
                                  Conscious)
                          (select |patient:management-info:conscious-status|
                                  Unconscious)))
                 POL_4.Comb.NA)))
  (let ((a!2 (and (or a!1 POL_1.Comb.NA)
                  (or (not (select party-type Insurance)) POL_2.Comb.NA)
                  (select role External)
                  (select party-type Pharmacy)
                  POL_3.Comb.D))
        (a!5 (and (or (not (select |patient:management-info:status|
                                   AvailableNurseDoctor))
                      POL_1.Comb.NA)
                  a!3
                  a!4
                  (select |patient:management-info:status|
                          NotAvailableNurseDoctor)
                  (select role Doctor)
                  (select position SeniorConsultant)
                  (or (select |patient:management-info:conscious-status|
                              Conscious)
                      (select |patient:management-info:conscious-status|
                              Unconscious))
                  POL_4.Comb.D))
        (a!7 (and (or (not (select |patient:management-info:status|
                                   AvailableNurseDoctor))
                      POL_1.Comb.NA)
                  a!3
                  a!4
                  a!6
                  (select |patient:management-info:status|
                          NotAvailableNurseDoctor)
                  (select role Doctor)
                  (select position SeniorConsultant)
                  POL_5.Comb.D)))
    (or (and (select |patient:management-info:status| AvailableNurseDoctor)
             (or (select role Doctor) (select role Nurse))
             POL_1.Comb.D)
        (and (or a!1 POL_1.Comb.NA)
             (select role External)
             (select party-type Insurance)
             POL_2.Comb.D)
        a!2
        a!5
        a!7)))
  (select action-type Read))


Cần có 1 giả định

Có thể set các giá trị còn lại là false được không?

Tất cả chỉ là sự bắt đầu

Công việc của mình là thay thế

Hiện tại chỉ hỗ trợ Bag cho kiểu String

Những kiểu khác nhưn Integer, Real sẽ là Single

Phải duyệt trên biểu thức và thay thế
	Dựa vào quan sát trên biểu thức để làm phép thay thế thành true / false
		Request phải làm phức tạp hơn rồi.

(let ((a!1 (and (or (and (select |patient:management-info:status|
                                 AvailableNurseDoctor)))
                (or (and (select role Doctor)) (and (select role Nurse)))))
      (a!2 (and (or (and (select role Doctor)))
                (not false)
                (not (= (store user-id Doctor-Patient true)
                        |patient:record:assigned-doctor-id|))
                (not (or card_user-id
                         |card_\|patient:record:assigned-doctor-id\||))))
      (a!3 (and (or (and (select role Nurse)))
                (not false)
                (not (= (store user-id Doctor-Patient true)
                        |patient:record:assigned-nurse-id|))
                (not (or card_user-id
                         |card_\|patient:record:assigned-nurse-id\||))))
      (a!5 (and (or (and (select role External) (select party-type Insurance)))))
      (a!6 (or (and (and true (not false))
                    (not (select resource-path Patient.Record.Billing))
                    (not (or card_resource-path)))
               false))
      (a!8 (and (or (and (select role External) (select party-type Pharmacy)))))
      (a!9 (or (and (and true (not false))
                    (not (select resource-path Patient.Record.Medicines))
                    (not (or card_resource-path)))
               false))
      (a!11 (or (and (select |patient:management-info:status|
                             NotAvailableNurseDoctor)
                     (select role Doctor)
                     (select position SeniorConsultant))))
      (a!13 (or (not (= location |patient:record:department|))
                (< current-time |patient:emgergency-agreement:start-time|)
                (> current-time |patient:emgergency-agreement:end-time|)))
      (a!18 (or (and (and true (not false))
                     (not (= location |patient:record:department|))
                     (not (or card_location
                              |card_\|patient:record:department\||)))
                false)))
(let ((a!4 (and (or (not a!1) (and (and false false) false)) (not false)))
      (a!12 (and a!11
                 (or (and (select |patient:management-info:conscious-status|
                                  Conscious))
                     (and (select |patient:management-info:conscious-status|
                                  Unconscious)))))
      (a!14 (or (not (= |patient:emgergency-agreement:medical-staff|
                        (store user-id Doctor-Patient true)))
                a!13))
      (a!16 (or (not (= |patient:emgergency-agreement:medical-staff|
                        (store user-id Doctor-Patient true)))
                (not (= |patient:emgergency-agreement:relative-passport-id|
                        |patient:management-info:relative-passport-id|))
                a!13)))
(let ((a!7 (and a!4 (or (not a!5) (and false false)) (not false)))
      (a!15 (and (or (and (select |patient:management-info:conscious-status|
                                  Conscious)))
                 (not false)
                 a!14
                 (not (or |card_\|patient:emgergency-agreement:medical-staff\||
                          card_user-id
                          card_location
                          |card_\|patient:record:department\||
                          card_current-time
                          |card_\|patient:emgergency-agreement:start-time\||
                          card_current-time
                          |card_\|patient:emgergency-agreement:end-time\||))))
      (a!17 (and (or (and (select |patient:management-info:conscious-status|
                                  Unconscious)))
                 (not false)
                 a!16
                 (not (or |card_\|patient:emgergency-agreement:medical-staff\||
                          card_user-id
                          |card_\|patient:emgergency-agreement:relative-passport-id\||
                          |card_\|patient:management-info:relative-passport-id\||
                          card_location
                          |card_\|patient:record:department\||
                          card_current-time
                          |card_\|patient:emgergency-agreement:start-time\||
                          card_current-time
                          |card_\|patient:emgergency-agreement:end-time\||)))))
(let ((a!10 (and a!7 (or (not a!8) (and false false)) (not false))))
(let ((a!19 (and a!10
                 (or (not a!12) (and (and false false) false))
                 (not false)
                 a!11
                 (not false)
                 a!18)))
  (and (or (and (select resource-type Patient.Record) (select action-type Read)))
       (not false)
       (or (and a!1 (not false) (or a!2 a!3 false))
           (and a!4 a!5 (not false) a!6)
           (and a!7 a!8 (not false) a!9)
           (and a!10 a!12 (not false) (or a!15 a!17 false))
           a!19)))))))


(let ((a!1 (store (store (store role External true) Doctor false) Nurse false)))
(let ((a!2 (and (or (and (select |patient:management-info:status|
                                 AvailableNurseDoctor)))
                (or (and (select a!1 Doctor)) (and (select a!1 Nurse)))))
      (a!3 (or (and (select a!1 External)
                    (select (store party-type Insurance true) Insurance))))
      (a!6 (or (and (select a!1 External)
                    (select (store party-type Insurance true) Pharmacy))))
      (a!8 (or (and (select |patient:management-info:status|
                            NotAvailableNurseDoctor)
                    (select a!1 Doctor)
                    (select position SeniorConsultant)))))
(let ((a!4 (and (and (or (not a!2) POL_1.Comb.NA) (not false))
                (and a!3)
                (not false)
                POL_2.Comb.D))
      (a!5 (and (and (or (not a!2) POL_1.Comb.NA) (not false))
                (or (not (and a!3)) POL_2.Comb.NA)
                (not false)))
      (a!9 (and a!8
                (or (and (select |patient:management-info:conscious-status|
                                 Conscious))
                    (and (select |patient:management-info:conscious-status|
                                 Unconscious))))))
(let ((a!7 (and a!5 (or (not (and a!6)) POL_3.Comb.NA) (not false))))
(let ((a!10 (or (and a!2 (not false) POL_1.Comb.D)
                a!4
                (and a!5 (and a!6) (not false) POL_3.Comb.D)
                (and a!7 a!9 (not false) POL_4.Comb.D)
                (and a!7
                     (or (not a!9) POL_4.Comb.NA)
                     (not false)
                     a!8
                     (not false)
                     POL_5.Comb.D))))
  (and (or (and (select resource-type Patient.Record) (select action-type Read)))
       (not false)
       a!10))))))

(goals
(goal
  (select resource-type Patient.Record)
  (select action-type Read)
  (or (and (or (select role Doctor) (select role Nurse)) POL_1.Comb.P)
      (and (select role External) (select party-type Insurance) POL_2.Comb.P)
      (and (select role External) (select party-type Pharmacy) POL_3.Comb.P))
  (not (and (or (select role Doctor) (select role Nurse)) POL_1.Comb.D))
  (not (and (select role External) (select party-type Insurance) POL_2.Comb.D))
  (not (and (select role External) (select party-type Pharmacy) POL_3.Comb.D))
  (let ((a!1 (or (and (or (select role Doctor) (select role Nurse))
                      POL_1.Comb.IN_PD)
                 (and (select role External)
                      (select party-type Insurance)
                      POL_2.Comb.IN_PD)
                 (and (or (select role Doctor) (select role Nurse))
                      POL_1.Comb.IN_D)
                 (and (select role External)
                      (select party-type Insurance)
                      POL_2.Comb.IN_D)))
        (a!2 (or (and (or (select role Doctor) (select role Nurse))
                      POL_1.Comb.D)
                 (and (select role External)
                      (select party-type Insurance)
                      POL_2.Comb.D))))
    (not (and a!1 (not a!2))))
  (not (and (select role External) (select party-type Pharmacy) POL_3.Comb.IN_PD))
  (not (and (select role External) (select party-type Pharmacy) POL_3.Comb.IN_D)))
)

(let ((a!1 (or (and (or (select role Doctor) (select role Nurse)) POL_1.Comb.P)
               (and (select role External)
                    (select party-type Insurance)
                    POL_2.Comb.P)))
      (a!2 (or (and (or (select role Doctor) (select role Nurse)) POL_1.Comb.D)
               (and (select role External)
                    (select party-type Insurance)
                    POL_2.Comb.D)))
      (a!3 (and (or (select role Doctor) (select role Nurse))
                POL_1.Comb.IN_D
                (or (and (select role External)
                         (select party-type Insurance)
                         POL_2.Comb.IN_P)
                    (and (select role External)
                         (select party-type Insurance)
                         POL_2.Comb.P))))
      (a!4 (or (and (or (select role Doctor) (select role Nurse))
                    POL_1.Comb.IN_P)
               (and (or (select role Doctor) (select role Nurse)) POL_1.Comb.P)))
      (a!6 (or (and (or (select role Doctor) (select role Nurse))
                    POL_1.Comb.IN_D)
               (and (select role External)
                    (select party-type Insurance)
                    POL_2.Comb.IN_D)))
      (a!9 (or (and (or (select role Doctor) (select role Nurse)) POL_1.Comb.D)
               (and (select role External)
                    (select party-type Insurance)
                    POL_2.Comb.D)
               (and (select role External)
                    (select party-type Pharmacy)
                    POL_3.Comb.D)))
      (a!11 (or (and (or (select role Doctor) (select role Nurse))
                     POL_1.Comb.IN_P)
                (and (select role External)
                     (select party-type Insurance)
                     POL_2.Comb.IN_P))))
(let ((a!5 (or (and (or (select role Doctor) (select role Nurse))
                    POL_1.Comb.IN_PD)
               (and (select role External)
                    (select party-type Insurance)
                    POL_2.Comb.IN_PD)
               a!3
               (and (select role External)
                    (select party-type Insurance)
                    POL_2.Comb.IN_D
                    a!4))))
(let ((a!7 (and a!6 (not a!2) (not (and a!5 (not a!2)))))
      (a!10 (and a!6
                 (not a!2)
                 (not (and a!5 (not a!2)))
                 (or (and (select role External)
                          (select party-type Pharmacy)
                          POL_3.Comb.IN_P)
                     (and (select role External)
                          (select party-type Pharmacy)
                          POL_3.Comb.P)))))
(let ((a!8 (and a!1 (not a!2) (not (and a!5 (not a!2))) (not a!7))))
(let ((a!12 (and a!11 (not a!2) (not (and a!5 (not a!2))) (not a!7) (not a!8))))
(let ((a!13 (and (or (and a!5 (not a!2))
                     (and (select role External)
                          (select party-type Pharmacy)
                          POL_3.Comb.IN_PD)
                     a!10
                     (and (select role External)
                          (select party-type Pharmacy)
                          POL_3.Comb.IN_D
                          (or a!12 a!8)))
                 (not a!9))))
(let ((a!14 (and (or a!7
                     (and (select role External)
                          (select party-type Pharmacy)
                          POL_3.Comb.IN_D))
                 (not a!9)
                 (not a!13))))
  (and (select resource-type Patient.Record)
       (select action-type Read)
       (or a!8
           (and (select role External)
                (select party-type Pharmacy)
                POL_3.Comb.P))
       (not a!9)
       (not a!13)
       (not a!14)))))))))


Deny (and (select resource-type Patient.Record)
     (select action-type Read)
     (or POL_1.D POL_2.D POL_3.D POL_4.D POL_5.D))

Permit (goal
  (let ((a!1 (and (and (and (and PS_2.NA PS_5.NA) PS_8.NA) PS_12.NA)
                  PS_16.NA
                  PS_20.NA
                  PS_24.P))
        (a!2 (and (and (and (and PS_2.NA PS_5.NA) PS_8.NA) PS_12.NA) PS_16.NA)))
  (let ((a!3 (and (and (and (and a!2 PS_20.NA) PS_24.NA) PS_29.NA)
                  PS_34.NA
                  PS_37.NA
                  PS_42.P))
        (a!4 (and (and (and (and a!2 PS_20.NA) PS_24.NA) PS_29.NA) PS_34.NA)))
  (let ((a!5 (and (and (and (and a!4 PS_37.NA) PS_42.NA) PS_46.NA)
                  PS_51.NA
                  PS_55.NA
                  PS_59.P))
        (a!6 (and (and (and (and a!4 PS_37.NA) PS_42.NA) PS_46.NA) PS_51.NA)))
  (let ((a!7 (and (and (and (and a!6 PS_55.NA) PS_59.NA) PS_63.NA)
                  PS_67.NA
                  PS_72.NA
                  PS_77.P))
        (a!8 (and (and (and (and a!6 PS_55.NA) PS_59.NA) PS_63.NA) PS_67.NA)))
  (let ((a!9 (and (and (and (and a!8 PS_72.NA) PS_77.NA) PS_83.NA)
                  PS_89.NA
                  PS_95.NA
                  PS_101.P))
        (a!10 (and (and (and (and a!8 PS_72.NA) PS_77.NA) PS_83.NA) PS_89.NA)))
    (or PS_2.P
        (and PS_2.NA PS_5.P)
        (and PS_2.NA PS_5.NA PS_8.P)
        (and PS_2.NA PS_5.NA PS_8.NA PS_12.P)
        (and (and PS_2.NA PS_5.NA) PS_8.NA PS_12.NA PS_16.P)
        (and (and (and PS_2.NA PS_5.NA) PS_8.NA) PS_12.NA PS_16.NA PS_20.P)
        a!1
        (and a!2 PS_20.NA PS_24.NA PS_29.P)
        (and (and a!2 PS_20.NA) PS_24.NA PS_29.NA PS_34.P)
        (and (and (and a!2 PS_20.NA) PS_24.NA) PS_29.NA PS_34.NA PS_37.P)
        a!3
        (and a!4 PS_37.NA PS_42.NA PS_46.P)
        (and (and a!4 PS_37.NA) PS_42.NA PS_46.NA PS_51.P)
        (and (and (and a!4 PS_37.NA) PS_42.NA) PS_46.NA PS_51.NA PS_55.P)
        a!5
        (and a!6 PS_55.NA PS_59.NA PS_63.P)
        (and (and a!6 PS_55.NA) PS_59.NA PS_63.NA PS_67.P)
        (and (and (and a!6 PS_55.NA) PS_59.NA) PS_63.NA PS_67.NA PS_72.P)
        a!7
        (and a!8 PS_72.NA PS_77.NA PS_83.P)
        (and (and a!8 PS_72.NA) PS_77.NA PS_83.NA PS_89.P)
        (and (and (and a!8 PS_72.NA) PS_77.NA) PS_83.NA PS_89.NA PS_95.P)
        a!9
        (and a!10 PS_95.NA PS_101.NA PS_107.P)
        (and a!10 PS_95.NA PS_101.NA PS_107.NA PS_110.P))))))))