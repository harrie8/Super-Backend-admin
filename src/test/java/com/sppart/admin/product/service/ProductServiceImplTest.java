package com.sppart.admin.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.sppart.admin.objectstorage.service.ObjectStorageService;
import com.sppart.admin.product.domain.mapper.ProductMapper;
import com.sppart.admin.product.dto.ProductSearchCondition;
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

//    @Test
//    @DisplayName("전시 ID들로 전시들을 삭제하는 테스트")
//    void bulkDeleteByIdsTest() {
//        //given
//        var ids = Set.of(1L, 2L);
//        var beforeExhibitionCount = productMapper.countAll();
//        var beforeProductExhibitionCount = productExhibitionMapper.countAll();
//
//        //when
//        var actual = productService.bulkDeleteByIds(ids);
//
//        //then
//        var afterExhibitionCount = productMapper.countAll();
//        var afterProductExhibitionCount = productExhibitionMapper.countAll();
//        assertAll(() -> {
//            assertEquals(ids.size(), actual.getExhibitionDeleteCount());
//            assertEquals(beforeExhibitionCount, afterExhibitionCount + ids.size());
//            assertEquals(beforeProductExhibitionCount, afterProductExhibitionCount + 5);
//        });
//    }
//
//    @Test
//    @DisplayName("전시 ID가 없다면 전시와 작품전시를 삭제하지 않는 테스트")
//    void bulkDeleteByNoIdsTest() {
//        //given
//        var ids = new HashSet<Long>();
//        var beforeExhibitionCount = productMapper.countAll();
//        var beforeProductExhibitionCount = productExhibitionMapper.countAll();
//
//        //when
//        var actual = productService.bulkDeleteByIds(ids);
//
//        //then
//        var afterExhibitionCount = productMapper.countAll();
//        var afterProductExhibitionCount = productExhibitionMapper.countAll();
//        assertAll(() -> {
//            assertEquals(ids.size(), actual.getProductExhibitionDeleteCount());
//            assertEquals(ids.size(), actual.getExhibitionDeleteCount());
//            assertEquals(beforeExhibitionCount, afterExhibitionCount);
//            assertEquals(beforeProductExhibitionCount, afterProductExhibitionCount);
//        });
//    }
//
//    @Test
//    @DisplayName("전시 ID로 전시 노출 상태 변경 테스트")
//    void updateOnlyDisplayTest() {
//        //given
//        var exhibitionId = 1L;
//        var expected = 0;
//        var req = RequestUpdateExhibitionDisplay.builder()
//                .isDisplay(expected)
//                .build();
//
//        //when
//        productService.updateOnlyDisplay(exhibitionId, req);
//
//        //then
//        var actual = productMapper.findById(exhibitionId).get().getIsDisplay();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 전시 ID로 전시 노출 상태 변경 테스트")
//    void updateOnlyDisplayWithNotExistsExhibitionTest() {
//        //given
//        var exhibitionId = 100L;
//        var req = RequestUpdateExhibitionDisplay.builder()
//                .isDisplay(0)
//                .build();
//
//        //expected
//        assertThrows(SuperpositionAdminException.class, () -> productService.updateOnlyDisplay(exhibitionId, req));
//    }
//
//    @Test
//    @DisplayName("전시 상세 조회 시 전시 상세 정보와 해당 전시에 참여한 작품들 정보를 반환하는 테스트")
//    void getByIdWithParticipatedProductsTest() {
//        //given
//        var exhibitionId = 1L;
//        String[] productExtracting = {"title", "basicView", "qrView", "likeCount", "orderCount"};
//
//        //when
//        var actual = productService.getByIdWithParticipatedProducts(exhibitionId);
//
//        //then
//        assertAll(() -> {
//            assertEquals(exhibitionId, actual.getExhibitionId());
//            assertEquals("Christmas Tree and Neapolitan Baroque Crèche", actual.getTitle());
//            assertThat(actual.getProducts())
//                    .hasSize(4)
//                    .extracting(productExtracting)
//                    .containsExactlyInAnyOrder(
//                            tuple(
//                                    "roses",
//                                    21,
//                                    2,
//                                    2,
//                                    3),
//                            tuple(
//                                    "highway",
//                                    10,
//                                    5,
//                                    3,
//                                    1),
//                            tuple(
//                                    "christmas in my hand",
//                                    6,
//                                    3,
//                                    1,
//                                    0),
//                            tuple(
//                                    "You shine, protect the light",
//                                    3,
//                                    0,
//                                    0,
//                                    0)
//                    );
//        });
//    }
//
//    @Test
//    @DisplayName("전시에 참여한 작품들이 없는 전시 상세 조회 시 전시 상세 정보만 반환하는 테스트")
//    void getByIdWithParticipatedProductsWithNotExistsParticipatedProductsTest() {
//        //given
//        var exhibitionId = 5L;
//
//        //when
//        var actual = productService.getByIdWithParticipatedProducts(exhibitionId);
//
//        //then
//        assertAll(() -> {
//            assertEquals(exhibitionId, actual.getExhibitionId());
//            assertEquals("Art for the Millions: American Culture and Politics in the 1930s", actual.getTitle());
//            assertEquals("The 1930s was a decade of political and social upheaval in the United States,",
//                    actual.getSubHeading());
//            assertEquals("MET", actual.getLocation());
//            assertEquals(LocalDate.parse("2023-09-07"), actual.getStartDate());
//            assertEquals(LocalDate.parse("2023-12-10"), actual.getEndDate());
//            assertEquals(ExhibitionStatus.end, actual.getStatus());
//            assertEquals("1930s.jpg", actual.getPoster());
//            assertThat(actual.getProducts())
//                    .hasSize(0);
//        });
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 전시 ID로 전시 상세 조회 시 예외 발생하는 테스트")
//    void getByIdWithParticipatedProductsWithNotExistsIdTest() {
//        //given
//        var notExistsExhibitionId = 100L;
//
//        //expected
//        assertThrows(SuperpositionAdminException.class,
//                () -> productService.getByIdWithParticipatedProducts(notExistsExhibitionId));
//    }
//
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