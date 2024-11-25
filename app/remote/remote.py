from flask import Flask, request, jsonify,send_file
import mysql.connector
import bcrypt
from mysql.connector import Error
from diffusers import StableDiffusionPipeline
import torch
from io import BytesIO
from flask_cors import CORS
from PIL import Image

app = Flask(__name__)

app.config['MAX_CONTENT_LENGTH'] = 100 * 1024 * 1024  # 100MB
app.config['TIMEOUT'] = 60 * 5

# MySQL 数据库连接配置
def get_db_connection():
    return mysql.connector.connect(
        host="172.172.52.124",  # MySQL 服务器地址
        user="root",            # 数据库用户名
        password="AIdrawingzyc123...sdahb",  # 数据库密码
        database="draw"         # 选择的数据库
    )



CORS(app)

# 加载模型
model = StableDiffusionPipeline.from_pretrained(
    "CompVis/stable-diffusion-v1-4",
    torch_dtype=torch.float16,
    revision="fp16"
).to("cuda")


@app.route('/imgToImg', methods=['POST'])
def generator2():
    try:
        # 获取请求中的数据
        positive_prompt = request.form.get('positivePrompt', '')
        negative_prompt = request.form.get('negativePrompt', '')
        guidance_scale = float(request.form.get('guidanceScale', 7.5))
        num_inference_steps = int(request.form.get('numInferenceSteps', 10))
        height = int(request.form.get('height', 512))
        width = int(request.form.get('width', 512))
        seed = request.form.get('seed', None)
        seed = int(seed) if seed is not None else None
        #num_inference_steps = 5
        print(positive_prompt)
        print(num_inference_steps)
        # 获取上传的图片比特流
        image_file = request.files.get('image')

        # 检查是否提供了图片
        if not image_file:
            return jsonify({"error": "No image file provided"}), 400

        # 将上传的图片加载到 PIL 图像对象
        image = Image.open(image_file.stream)

        gen_image = model(positive_prompt, image = image,negative_prompt=negative_prompt, guidance_scale=guidance_scale,
             num_inference_steps=num_inference_steps, height=height, width=width, seed=seed).images[0]
        # gen_image = image
        # 将修改后的图像保存到比特流
        img_io = BytesIO()
        gen_image.save(img_io, 'PNG')
        img_io.seek(0)

        # 返回图像，作为比特流
        return send_file(img_io, mimetype='image/png')

    except Exception as e:
        print(f"Error generating image: {e}")
        return jsonify({"error": str(e)}), 500

@app.route('/generate', methods=['POST'])
def generator():
    try:
        # 获取数据
        positive_prompt = request.json.get('positivePrompt', '')
        negative_prompt = request.json.get('negativePrompt', '')
        guidance_scale = request.json.get('guidanceScale', 7.5)  # 默认 7.5
        num_inference_steps = request.json.get('numInferenceSteps', 10)  # 默认 50 步
        height = request.json.get('height', 512)  # 默认 512
        width = request.json.get('width', 512)    # 默认 512
        seed = request.json.get('seed', None)  # 可选的种子
        # num_inference_steps = 5

        if not positive_prompt:
            return jsonify({"error": "Prompt is required"}), 400

        # print(f"Generating image for prompt: {prompt}")

        # image = Image.open("123.png")
        image = model(positive_prompt, negative_prompt=negative_prompt, guidance_scale=guidance_scale,
             num_inference_steps=num_inference_steps, height=height, width=width, seed=seed).images[0]
        #image.show()
        # image.show()
        img_io = BytesIO()
        image.save(img_io, 'PNG')
        img_io.seek(0)

        # 返回图像
        return send_file(img_io, mimetype='image/png')
    except Exception as e:
        print(f"Error generating image: {e}")
        return jsonify({"error": str(e)}), 500

# 用户注册接口
@app.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    name = data.get('name')
    password = data.get('password')
    email = data.get('email')

    if not name or not password or not email:
        return jsonify({"message": "用户名、密码和邮箱不能为空"}), 400

    # 加密密码
    hashed_password = encrypt_password(password)

    # 将用户数据插入数据库
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        # 插入新用户数据
        query = "INSERT INTO users (name, password, email) VALUES (%s, %s, %s)"
        cursor.execute(query, (name, hashed_password, email))
        connection.commit()

        return jsonify({"message": "用户注册成功"}), 201

    except Error as e:
        return jsonify({"message": f"数据库错误: {str(e)}"}), 500
    finally:
        if connection:
            cursor.close()
            connection.close()

# 用户登录接口
@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    name = data.get('name')
    password = data.get('password')

    if not name or not password:
        return jsonify({"message": "用户名和密码不能为空"}), 400

    # 查询数据库中的用户
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        query = "SELECT * FROM users WHERE name = %s"
        cursor.execute(query, (name,))
        user = cursor.fetchone()

        if user and check_password(password, user['password']):
            return jsonify({"message": "登录成功"}), 200
        else:
            return jsonify({"message": "用户名或密码错误"}), 401

    except Error as e:
        return jsonify({"message": f"数据库错误: {str(e)}"}), 500
    finally:
        if connection:
            cursor.close()
            connection.close()

# 根据用户名查询用户
@app.route('/get_user_by_name', methods=['GET'])
def get_user_by_name():
    name = request.args.get('name')
    if not name:
        return jsonify({"message": "用户名不能为空"}), 400

    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        query = "SELECT * FROM users WHERE name = %s"
        cursor.execute(query, (name,))
        user = cursor.fetchone()

        if user:
            return jsonify({"user": user}), 200
        else:
            return jsonify({"message": "用户不存在"}), 404

    except Error as e:
        return jsonify({"message": f"数据库错误: {str(e)}"}), 500
    finally:
        if connection:
            cursor.close()
            connection.close()

# 根据邮箱查询用户
@app.route('/get_user_by_email', methods=['GET'])
def get_user_by_email():
    email = request.args.get('email')
    if not email:
        return jsonify({"message": "邮箱不能为空"}), 400

    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        query = "SELECT * FROM users WHERE email = %s"
        cursor.execute(query, (email,))
        user = cursor.fetchone()

        if user:
            return jsonify({"user": user}), 200
        else:
            return jsonify({"message": "用户不存在"}), 404

    except Error as e:
        return jsonify({"message": f"数据库错误: {str(e)}"}), 500
    finally:
        if connection:
            cursor.close()
            connection.close()

# 修改密码
@app.route('/update_password', methods=['PUT'])
def update_password():
    data = request.get_json()
    name = data.get('name')
    old_password = data.get('old_password')
    new_password = data.get('new_password')

    if not name or not old_password or not new_password:
        return jsonify({"message": "用户名、旧密码和新密码不能为空"}), 400

    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        query = "SELECT * FROM users WHERE name = %s"
        cursor.execute(query, (name,))
        user = cursor.fetchone()

        if user and check_password(old_password, user['password']):
            hashed_new_password = encrypt_password(new_password)

            update_query = "UPDATE users SET password = %s WHERE name = %s"
            cursor.execute(update_query, (hashed_new_password, name))
            connection.commit()

            return jsonify({"message": "密码修改成功"}), 200
        else:
            return jsonify({"message": "用户名或旧密码错误"}), 400

    except Error as e:
        return jsonify({"message": f"数据库错误: {str(e)}"}), 500
    finally:
        if connection:
            cursor.close()
            connection.close()

# 修改个人信息（包括邮箱和用户名）
@app.route('/update_profile', methods=['PUT'])
def update_profile():
    data = request.get_json()
    name = data.get('name')
    new_name = data.get('new_name')
    new_email = data.get('new_email')
    new_password = data.get('new_password')
    hash_password = encrypt_password(new_password)

    if not name or not new_name or not new_email:
        return jsonify({"message": "用户名、新用户名和新邮箱不能为空"}), 400

    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        # 更新用户名和邮箱
        update_query = "UPDATE users SET name = %s, email = %s, password = %s WHERE name = %s"
        cursor.execute(update_query, (new_name, new_email, hash_password, name))
        connection.commit()

        return jsonify({"message": "个人信息修改成功"}), 200

    except Error as e:
        return jsonify({"message": f"数据库错误: {str(e)}"}), 500
    finally:
        if connection:
            cursor.close()
            connection.close()


def encrypt_password(plain_password: str) -> str:
    # return plain_password
    return bcrypt.hashpw(plain_password.encode('utf-8'), bcrypt.gensalt()).decode('utf-8')


def check_password(plain_password: str, hashed_password: str) -> bool:
    # return plain_password == hashed_password
    return bcrypt.checkpw(plain_password.encode('utf-8'), hashed_password.encode('utf-8'))


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
