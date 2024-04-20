package com.sppart.admin.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sppart.admin.exception.SuperpositionAdminException;
import com.sppart.admin.objectstorage.service.ObjectStorageService;
import com.sppart.admin.product.domain.mapper.ProductMapper;
import com.sppart.admin.product.dto.ProductSearchCondition;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@ActiveProfiles("test")
class ProductServiceImplTest {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductServiceImplTest(ProductMapper productMapper) {
        this.productMapper = productMapper;
        this.productService = new ProductServiceImpl(productMapper,
                new ObjectStorageService() {
                    @Override
                    public String uploadFile(MultipartFile file) {
                        return "";
                    }

                    @Override
                    public List<String> uploadFiles(List<MultipartFile> files) {
                        return List.of();
                    }

                    @Override
                    public void delete(String url) {

                    }

                    @Override
                    public void delete(List<String> urls) {

                    }
                });
    }

    private final String[] extracting = {"productId", "title", "artistName", "tags", "description", "price"};

    @Test
    @DisplayName("모든 작품 조회 테스트")
    void getProductsWithTagsByConditionTest() {
        //given
        var condition = ProductSearchCondition.builder()
                .build();

        //when
        var actual = productService.getProductsByCondition(condition).getFindDomains();

        //then
        assertThat(actual)
                .hasSize(29)
                .extracting("productId")
                .containsExactlyInAnyOrder(
                        1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L,
                        22L, 23L, 24L, 25L, 26L, 27L, 28L, 29L
                );
    }

    @Test
    @DisplayName("작품명으로 작품 조회 테스트")
    void getProductsByTitleConditionTest() {
        //given
        var condition = ProductSearchCondition.builder()
                .title("roses")
                .build();

        //when
        var actual = productService.getProductsByCondition(condition).getFindDomains();

        //then
        assertThat(actual)
                .hasSize(1)
                .extracting(extracting)
                .containsExactlyInAnyOrder(
                        id1Product()
                );
    }

    @Test
    @DisplayName("작가 이름으로 작품 조회 테스트")
    void getProductsByArtistNameConditionTest() {
        //given
        var condition = ProductSearchCondition.builder()
                .artistName("문소")
                .build();

        //when
        var actual = productService.getProductsByCondition(condition).getFindDomains();

        //then
        assertThat(actual)
                .hasSize(12)
                .extracting(extracting)
                .containsExactlyInAnyOrder(
                        id1Product(),
                        id2Product(),
                        id3Product(),
                        id4Product(),
                        id5Product(),
                        id6Product(),
                        id7Product(),
                        id8Product(),
                        id9Product(),
                        id10Product(),
                        id11Product(),
                        id12Product()
                );
    }

    @Test
    @DisplayName("작품 코드로 작품 조회 테스트")
    void getProductsByProductIdConditionTest() {
        //given
        var condition = ProductSearchCondition.builder()
                .productId(1L)
                .build();

        //when
        var actual = productService.getProductsByCondition(condition).getFindDomains();

        //then
        assertThat(actual)
                .hasSize(1)
                .extracting(extracting)
                .containsExactlyInAnyOrder(
                        id1Product()
                );
    }

    @Test
    @DisplayName("작품 코드들로 작품들을 삭제하는 테스트")
    void bulkDeleteByIdsTest() {
        //given
        var ids = Set.of(1L, 2L);
        var beforeProductCount = productMapper.countAll();

        //when
        var actual = productService.bulkDeleteByIds(ids);

        //then
        var afterProductCount = productMapper.countAll();
        assertAll(() -> {
            assertEquals(ids.size(), actual.getExhibitionDeleteCount());
            assertEquals(beforeProductCount, afterProductCount + ids.size());
        });
    }

    @Test
    @DisplayName("작품 코드를 전달하지 않으면 작품을 삭제하지 않는 테스트")
    void bulkDeleteByNoIdsTest() {
        //given
        var ids = new HashSet<Long>();
        var beforeProductCount = productMapper.countAll();

        //when
        var actual = productService.bulkDeleteByIds(ids);

        //then
        var afterProductCount = productMapper.countAll();
        assertAll(() -> {
            assertEquals(ids.size(), actual.getProductExhibitionDeleteCount());
            assertEquals(ids.size(), actual.getExhibitionDeleteCount());
            assertEquals(beforeProductCount, afterProductCount);
        });
    }

    @Test
    @DisplayName("작품 상세 조회 시 작품 상세 정보, 태그, 전시 상태, 작품이 참여중인 전시 이름을 반환하는 테스트")
    void getDetailInfoByIdTest() {
        //given
        var productId = 1L;

        //when
        var actual = productService.getDetailInfoById(productId);

        //then
        assertAll(() -> {
            assertEquals(1L, actual.getProductId());
            assertEquals("6e305a70-0ed7-4f40-a59e-e28bb495887d.jpg", actual.getPicture());
            assertEquals("roses", actual.getTitle());
            assertEquals("문소", actual.getArtistName());
            assertEquals("나를 위로해주는 아름다운 장미, 그리고 음악과 함께 떠오르는 아련한 기억", actual.getDescription());
            assertEquals(250_000, actual.getPrice());
            assertEquals(21, actual.getBasicView());
            assertEquals(2, actual.getQrView());
            assertEquals(2, actual.getLikeCount());
            assertEquals(3, actual.getOrderCount());
            assertThat(actual.getTags())
                    .hasSize(3)
                    .containsExactlyInAnyOrder(
                            "청량한",
                            "맑은",
                            "아련한"
                    );
        });
    }

    @Test
    @DisplayName("전시에 참여하지 않은 작품 상세 조회 시 작품 상세 정보만 반환하는 테스트")
    void getDetailInfoByIdWithNotExistsParticipatedExhibitionsTest() {
        //given
        var productId = 5L;

        //when
        var actual = productService.getDetailInfoById(productId);

        //then
        assertAll(() -> {
            assertEquals(5L, actual.getProductId());
            assertEquals("72745895-04f3-4966-9263-932220f32e5f.jpg", actual.getPicture());
            assertEquals("Home sweet home", actual.getTitle());
            assertEquals("문소", actual.getArtistName());
            assertEquals("오늘의 힐링, 이보다 더 좋을 수 없어.", actual.getDescription());
            assertEquals(100_000, actual.getPrice());
            assertEquals(7, actual.getBasicView());
            assertEquals(4, actual.getQrView());
            assertEquals(1, actual.getLikeCount());
            assertEquals(1, actual.getOrderCount());
            assertThat(actual.getTags())
                    .hasSize(3)
                    .containsExactlyInAnyOrder(
                            "일상적인",
                            "평온한",
                            "컬러풀한"
                    );
        });
    }

    @Test
    @DisplayName("존재하지 않는 작품 ID로 작품 상세 조회 시 예외 발생하는 테스트")
    void getDetailInfoByIdWithNotProductIdTest() {
        //given
        var notExistsProductId = 100L;

        //expected
        assertThrows(SuperpositionAdminException.class,
                () -> productService.getDetailInfoById(notExistsProductId));
    }

//    @Test
//    @DisplayName("전시 생성 테스트")
//    void createTest() {
//        //given
//        var req = RequestCreateExhibition.builder()
//                .title("제목입니다.")
//                .subHeading("부제목입니다.")
//                .startDate(LocalDate.parse("2024-01-01"))
//                .endDate(LocalDate.parse("2024-05-01"))
//                .location("장소입니다.")
//                .status(ExhibitionStatus.current.name())
//                .productIds(Set.of(1L, 2L, 3L))
//                .build();
//        var poster = new MockMultipartFile("file", "test.txt", "text/plain",
//                "test file".getBytes(StandardCharsets.UTF_8));
//
//        //when
//        var createdExhibitionId = productService.create(req, poster);
//
//        //then
//        var actual = productMapper.findById(createdExhibitionId).get();
//        var productExhibitions = productExhibitionMapper.findByExhibitionId(createdExhibitionId);
//        assertAll(() -> {
//            assertEquals(req.getTitle(), actual.getTitle());
//            assertEquals(req.getSubHeading(), actual.getSubHeading());
//            assertEquals(req.getLocation(), actual.getLocation());
//            assertEquals(req.getStartDate(), actual.getStartDate());
//            assertEquals(req.getEndDate(), actual.getEndDate());
//            assertEquals(ExhibitionStatus.findByName(req.getStatus()), actual.getStatus());
//            assertNotNull(actual.getPoster());
//            assertThat(productExhibitions)
//                    .hasSize(3)
//                    .extracting("productId", "exhibitionId")
//                    .containsExactlyInAnyOrder(
//                            tuple(1L, createdExhibitionId),
//                            tuple(2L, createdExhibitionId),
//                            tuple(3L, createdExhibitionId)
//                    );
//        });
//    }

    private Tuple id1Product() {
        return tuple(
                1L,
                "roses",
                "문소",
                Set.of("청량한", "맑은", "아련한"),
                "나를 위로해주는 아름다운 장미, 그리고 음악과 함께 떠오르는 아련한 기억",
                250_000
        );
    }

    private Tuple id2Product() {
        return tuple(
                2L,
                "highway",
                "문소",
                Set.of("청량한", "일상적인", "자유로운"),
                "여행의 설레임과 즐거움 속에 언제나 함께하는 사랑스러운 너.",
                350_000
        );
    }

    private Tuple id3Product() {
        return tuple(
                3L,
                "우리들",
                "문소",
                Set.of("키치한", "일상적인"),
                "맛있는 빵가게는 그냥 지나칠 수 없지!",
                100000
        );
    }

    private Tuple id4Product() {
        return tuple(
                4L,
                "How Deep Is Your Love?",
                "문소",
                Set.of("청량한", "컬러풀한", "평화로운"),
                "당신의 사랑은 어느 정도 일까?",
                100000
        );
    }

    private Tuple id5Product() {
        return tuple(
                5L,
                "Home sweet home",
                "문소",
                Set.of("일상적인", "평온한", "컬러풀한"),
                "오늘의 힐링, 이보다 더 좋을 수 없어.",
                100000
        );
    }

    private Tuple id6Product() {
        return tuple(
                6L,
                "fall",
                "문소",
                Set.of("따뜻한", "귀여운", "평온한"),
                "바람이 불어오는 곳으로 걸어가다 보면 찾을 수 있겠지.",
                250000
        );
    }

    private Tuple id7Product() {
        return tuple(
                7L,
                "flower child",
                "문소",
                Set.of("평온한", "귀여운", "컬러풀한"),
                "작고 예쁘고 소중한 아이",
                200000
        );
    }

    private Tuple id8Product() {
        return tuple(
                8L,
                "summer2 (rainy day)",
                "문소",
                Set.of("시원한", "상쾌한", "즐거운"),
                "비가 내리던 휴양지에서",
                100000
        );
    }

    private Tuple id9Product() {
        return tuple(
                9L,
                "summer4 (icecream)",
                "문소",
                Set.of("달콤한", "컬러풀한", "일상적인"),
                "어느 여름날",
                200000
        );
    }

    private Tuple id10Product() {
        return tuple(
                10L,
                "city light",
                "문소",
                Set.of("도시의", "빛나는", "자유로운"),
                "댕댕이와 함께하는 신나는 드라이브",
                200000
        );
    }

    private Tuple id11Product() {
        return tuple(
                11L,
                "winter1 (with cat)",
                "문소",
                Set.of("귀여운", "잔망스러운"),
                "첫 눈처럼 온 아이",
                200000
        );
    }

    private Tuple id12Product() {
        return tuple(
                12L,
                "winter2 (with dog)",
                "문소",
                Set.of("포근한", "아기자기한"),
                "한겨울",
                200000
        );
    }
}