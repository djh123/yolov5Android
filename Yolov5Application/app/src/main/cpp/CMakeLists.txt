# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fopenmp")
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fopenmp")
if(DEFINED ANDROID_NDK_MAJOR AND ${ANDROID_NDK_MAJOR} GREATER 20)
    set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -static-openmp")
endif()


add_library( # Sets the name of the library.
        native
        # Sets the library as a shared library.
        SHARED
        native.cpp
        YoloV5.cpp
        )

include_directories(
        ncnn/include
)


find_library(log-lib log)
find_library(android-lib android)

find_library(jnigraphics-lib jnigraphics)
add_library( ncnn STATIC IMPORTED )
set_target_properties( # Specifies the target library.
        ncnn

        # Specifies the parameter you want to define.
        PROPERTIES IMPORTED_LOCATION

        # Provides the path to the library you want to import.
        ${CMAKE_SOURCE_DIR}/ncnn/${ANDROID_ABI}/libncnn.a )


# ncnnvulkan
# add_library(glslang STATIC IMPORTED)
# add_library(OGLCompiler STATIC IMPORTED)
# add_library(OSDependent STATIC IMPORTED)
# add_library(SPIRV STATIC IMPORTED)
# set_target_properties(glslang PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/ncnnvulkan/${ANDROID_ABI}/libglslang.a)
# set_target_properties(OGLCompiler PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/ncnnvulkan/${ANDROID_ABI}/libOGLCompiler.a)
# set_target_properties(OSDependent PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/ncnnvulkan/${ANDROID_ABI}/libOSDependent.a)
# set_target_properties(SPIRV PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/ncnnvulkan/${ANDROID_ABI}/libSPIRV.a)


include_directories(
        ${CMAKE_SOURCE_DIR}/opencv/include/
)
add_library(libopencv_java4 STATIC IMPORTED)
set_target_properties(
        libopencv_java4
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/opencv/${ANDROID_ABI}/libopencv_java4.so
        )
target_link_libraries(
        native
        ${log-lib}
        ${vulkan-lib}
        ${android-lib}
        ${jnigraphics-lib}
        libopencv_java4)

target_link_libraries(
        native
        ${log-lib}
        ${vulkan-lib}
        ${android-lib}
        ${jnigraphics-lib}
        ncnn)
